import Vue from 'https://cdn.jsdelivr.net/npm/vue@2'
import axios from 'https://cdn.bootcss.com/axios/0.19.0-beta.1/axios.js'
//Vue.prototype.$axios = axios
axios.defaults.baseURL = 'http://localhost:8443/api/hdfs'  //关键代码
Vue.config.productionTip = false

function getForderView(vue){
    var dir = vue.dir;

    this.axios
        .get('/director/list?directorName='+dir,{

        })
        .then(function (res){
            vue = res.data.data;

            for(var i = 0 ;i < res.data.data.length;i++){
                vue[i].subDir = vue[i].subDir.toString().substring()
            }
            console.log(res);
        })
        .catch(function (err){
            // handle error
            console.log(error);
        })
}
function uploadFileFromLocal(vue){
    this.axios
        .post({

        })
        .then(function (res){

            console.log(res);
        })
        .catch(function (err){
            // handle error
            console.log(error);
        })
}
function downloadFileFromLocal(vue){
    this.axios
        .get({

        })
        .then(function (res){

            console.log(res);
        })
        .catch(function (err){
            // handle error
            console.log(error);
        })
}
function deleteFile(vue){
    this.axios
        .post({

        })
        .then(function (res){

            console.log(res);
        })
        .catch(function (err){
            // handle error
            console.log(error);
        })
}




