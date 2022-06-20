package com.suicuntong.sct.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suicuntong.sct.entity.File;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ESDao {

    private static String host;
    private static int port;
    private static String scheme;
    private static int doxId;
    private static String idxname;

    @Value("${ES.host}")
    public void setHost(String host){
        this.host = host;
    }

    @Value("${ES.port}")
    public void setPort(int port){
        this.port = port;
    }

    @Value("${ES.scheme}")
    public void setScheme(String scheme){
        this.scheme = scheme;
    }

    @Value("${ES.idxname}")
    public void setIdxname(String idxname){
        this.idxname = idxname;
    }

    String FIELD_FILE_NAME = "fileName";
    String FIELD_FILE_TYPE = "fileType";
    String FIELD_FILE_SIZE = "fileSize";
    String FIELD_FILE_DATE = "fileModificationTime";
    String FIELD_FILE_CONTENT = "fileContent";


    public RestHighLevelClient establish(){
        RestHighLevelClient esClient = null;
        // 创建ES客户端
        try {
            esClient = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(host, port, scheme))
            );

        }catch (Exception e) {
            e.printStackTrace();
        }
        return esClient;
    }
    public void shutdown(RestHighLevelClient esClient){
        // 关闭ES客户端
        try{
            esClient.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String docJson) throws Exception {
        RestHighLevelClient esClient = establish();
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index(idxname);
        //设置文档 ID,不设置则系统生成
        indexRequest.id(String.valueOf(doxId++));


        indexRequest.source(docJson, XContentType.JSON);

        IndexResponse response = esClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println("文档新增是否成功 " + response.getResult());
        shutdown(esClient);
    }

    public List<Map<String,Object>> match(String search_query, String search_type, String file_type, String sort_type) throws Exception {

        RestHighLevelClient esClient = establish();
        SearchRequest searchRequest = new SearchRequest(idxname);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 指定文件类型
        filetype_search_query(boolQueryBuilder,file_type);

        if (search_type.equalsIgnoreCase("FILE_NAME_SEARCH"))
            // 文件名搜索
            filename_search_query(boolQueryBuilder,search_query);
        else
            // 全文搜索
            fulltext_search_query(boolQueryBuilder,search_query);

        // 设置输出文档数量
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);

        // 指定排序条件
        sorttype_search_query(searchSourceBuilder,sort_type);

        // 设置高亮显示
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.preTags("<span class=\"glnow\">");
        highlightBuilder.postTags("</span>");
        highlightBuilder.field(FIELD_FILE_NAME).field(FIELD_FILE_TYPE).field(FIELD_FILE_SIZE).field(FIELD_FILE_DATE).field(FIELD_FILE_CONTENT);
        searchSourceBuilder.highlighter(highlightBuilder);

        // 将查询对象添加到 sourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);
        // 将 sourceBuilder 添加到查询请求
        searchRequest.source(searchSourceBuilder);
        // 搜索并获取返回结果
        SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println("搜索用时 " + searchResponse.getTook());
        SearchHits searchHits = searchResponse.getHits();
        System.out.println("搜索结果总数 " + searchHits.getTotalHits());
        float hitsMaxScore = searchHits.getMaxScore();
        System.out.println("搜索最高分数 " + hitsMaxScore);

        List<Map<String,Object>> finalResult = new ArrayList<>();
        int fileId = 0;
        SearchHit[] searchHit = searchHits.getHits();
        for (SearchHit hit : searchHit) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            // 添加score分数
            sourceAsMap.put("fileId",fileId++);
            if(sort_type.equalsIgnoreCase("SORT_BY_MATCH")){
                sourceAsMap.put("fileScore",hit.getScore());
                sourceAsMap.put("fileScorePercent",100*hit.getScore()/hitsMaxScore);
            }
            else{
                sourceAsMap.put("fileScore",0);
                sourceAsMap.put("fileScorePercent",0);
            }
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();

            for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
                StringBuilder highlightStr = new StringBuilder();
                Text[] texts = entry.getValue().getFragments();
                for (Text text : texts){
                    highlightStr.append(text.string());
                }
                // 替换高亮字符串
                hit.getSourceAsMap().put(entry.getKey(),highlightStr.toString());
            }
            for (Map.Entry<String, Object> entry : sourceAsMap.entrySet()) {
                System.out.println("Key " + entry.getKey() + "\t" + "Value " + entry.getValue());
            }
            finalResult.add(sourceAsMap);
        }
        shutdown(esClient);
        return finalResult;
    }

    public void create(RestHighLevelClient esClient, String idxname) throws Exception{

        CreateIndexRequest createIndexRequest = new CreateIndexRequest(idxname);
        // 设置 settings
        Settings.Builder settings = Settings.builder();
        settings.put("number_of_shards", 3);
        settings.put("number_of_replicas", 1);
        createIndexRequest.settings(settings);

        // 设置 mapping
        XContentBuilder source= JsonXContent.contentBuilder();
        source.startObject()
                .field("dynamic", "false")
                .startObject("properties")
                .startObject("name")
                .field("type","text")
                .field("index", "true")
                .field("analyzer", "ik_smart")
                .startObject("originalName")
                .field("type","text")
                .field("index", "true")
                .field("analyzer", "ik_smart")
                .endObject()
                .startObject("fileExtension")
                .field("type","keyword")
                .field("index", "true")
                .endObject()
                .startObject("isDir")
                .field("type","boolean")
                .field("index", "false")
                .endObject()
                .startObject("filePath")
                .field("type","keyword")
                .field("index", "false")
                .endObject()
                .startObject("originalPath")
                .field("type","keyword")
                .field("index", "false")
                .endObject()
                .startObject("type")
                .field("type","keyword")
                .field("index", "true")
                .endObject()
                .startObject("size")
                .field("type","long")
                .field("index", "true")
                .endObject()
                .startObject("modificationTime")
                .field("type","date")
                .field("format","yyyy-MM-dd hh:mm:ss")
                .field("index", "true")
                .endObject()
                .startObject("content")
                .field("type","text")
                .field("index", "true")
                .field("analyzer", "ik_smart")
                .endObject()
                .endObject()
                .endObject();

        createIndexRequest.mapping(source);

        CreateIndexResponse createIndexResponse = esClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

        //响应状态
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println("索引操作:" + acknowledged );
    }

    public long count(RestHighLevelClient esClient, String index) throws Exception {
        CountRequest countRequest = new CountRequest(index);
        CountResponse count = esClient.count(countRequest, RequestOptions.DEFAULT);
        System.out.println("索引为 " + index + " 的文档总数为: " + count.getCount());
        return count.getCount();
    }

    public void delete(RestHighLevelClient esClient, String idxname, String id) throws Exception {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.index(idxname);
        deleteRequest.id(id);

        DeleteResponse deleteResponse = esClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println("文档删除是否成功 " + deleteResponse.getResult());
    }

    public static boolean exists(RestHighLevelClient esClient, String index, String id) throws Exception {
        GetRequest getRequest = new GetRequest(index, id);
        boolean exists = esClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println("索引为 " + index + "的文档 " + id + " 是否存在：" + exists);
        return exists;
    }

    public void insertBatch(RestHighLevelClient esClient, String idxname, String startid, List<File> fileList) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();

        for(File f: fileList){
            String docJson = new ObjectMapper().writeValueAsString(f);
            System.out.println("docJson内容 " + docJson);
            bulkRequest.add(new IndexRequest().index(idxname).id(startid).source(docJson, XContentType.JSON));
            startid = String.valueOf(Integer.parseInt(startid) + 1);
        }
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse response = esClient.bulk(bulkRequest,RequestOptions.DEFAULT);
        System.out.println("文档批量新增用时 " + response.getTook() + response.status());
    }

    public void update(RestHighLevelClient esClient, String idxname, String id, File f) throws Exception {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(idxname);
        updateRequest.id(id);

        ObjectMapper mapper = new ObjectMapper();
        String docJson = mapper.writeValueAsString(f);
        updateRequest.doc(docJson,XContentType.JSON);

        UpdateResponse response = esClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println("文档修改是否成功 " + response.getResult());
    }

    public String get_Json(RestHighLevelClient esClient, String idxname, String id) throws Exception {
        GetRequest request = new GetRequest();
        request.index(idxname);
        request.id(id);

        GetResponse response = esClient.get(request, RequestOptions.DEFAULT);
        String respondString = response.getSourceAsString();

        System.out.println(respondString);
        return respondString;
    }




    // 文件名搜索
    public void filename_search_query(BoolQueryBuilder boolQueryBuilder, String search_query){
        // 将搜索内容进行分割
        String[] queries = search_query.split(" ");
        for (String query : queries) {
            // matchPhrasePrefixQuery允许对最后一个词条前缀匹配
            boolQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery(FIELD_FILE_NAME, query));
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(FIELD_FILE_NAME, "*" + query + "*"));
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(FIELD_FILE_NAME, "*" + query));
            boolQueryBuilder.should(QueryBuilders.fuzzyQuery(FIELD_FILE_NAME, query).fuzziness(Fuzziness.ONE));
        }
        boolQueryBuilder.minimumShouldMatch(1);
    }

    // 全文搜素
    public void fulltext_search_query(BoolQueryBuilder boolQueryBuilder, String search_query){
        String[] queries = search_query.split(" ");
        for (String query : queries) {
            boolQueryBuilder.should(QueryBuilders.multiMatchQuery(query,FIELD_FILE_NAME,FIELD_FILE_CONTENT));
            boolQueryBuilder.should(QueryBuilders.queryStringQuery(query));

            boolQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery(FIELD_FILE_NAME, query));
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(FIELD_FILE_NAME, "*" + query + "*"));
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(FIELD_FILE_NAME, "*" + query));
            boolQueryBuilder.should(QueryBuilders.fuzzyQuery(FIELD_FILE_NAME, query).fuzziness(Fuzziness.ONE));

            boolQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery(FIELD_FILE_CONTENT, query));
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(FIELD_FILE_CONTENT, "*" + query + "*"));
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(FIELD_FILE_CONTENT, "*" + query));
            boolQueryBuilder.should(QueryBuilders.fuzzyQuery(FIELD_FILE_CONTENT, query).fuzziness(Fuzziness.ONE));

        }
        boolQueryBuilder.minimumShouldMatch(1);
    }

    // 根据文件类型指定搜索条件
    public void filetype_search_query(BoolQueryBuilder boolQueryBuilder, String file_type){
        switch (file_type){
            case "DOCX":
                boolQueryBuilder.must(QueryBuilders.termQuery(FIELD_FILE_TYPE,"docx"));
                break;
            case "TXT":
                boolQueryBuilder.must(QueryBuilders.termQuery(FIELD_FILE_TYPE,"txt"));
                break;
            default:
                break;
        }
    }

    // 根据排序类型来指定排序条件
    public void sorttype_search_query(SearchSourceBuilder searchSourceBuilder, String sort_type){
        switch (sort_type){
            case "SORT_BY_DATE":
                searchSourceBuilder.sort(SortBuilders.fieldSort(FIELD_FILE_DATE).order(SortOrder.DESC));
                break;
            case "SORT_BY_SIZE":
                searchSourceBuilder.sort(SortBuilders.fieldSort(FIELD_FILE_SIZE).order(SortOrder.DESC));
                break;
            default:
                searchSourceBuilder.sort(SortBuilders.scoreSort().order(SortOrder.DESC));
                break;
        }
    }

}
