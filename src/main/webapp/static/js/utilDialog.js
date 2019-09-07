

const utilDialog = {

}

utilDialog.utilConfigDialog = function (node,index,rowvalue,callback) {
  node.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    callback(true,rowvalue, node);
  }).catch(() => {
    callback(false,rowvalue, node);
  });
};

utilDialog.utilConfigDialogAndTitle = function (title,node,index,rowvalue,callback) {
  node.$confirm(title, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    callback(true,rowvalue, node);
}).catch(() => {
    callback(false,rowvalue, node);
});
};

utilDialog.likeAlert = function(node,res) {
  node.$alert(res, '提示', {
    confirmButtonText: '确定',
  });
};

utilDialog.alertAndCallBack=function (node,title,callbackMethod) {
  node.$alert(title, '提示', {
    confirmButtonText: '确定',
  }).then(()=>{
    callbackMethod(node,true);
  }).catch(()=>{
    callbackMethod(node, false);
  });
}


export default utilDialog;
