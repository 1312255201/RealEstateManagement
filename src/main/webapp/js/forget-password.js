/**
 * 提交忘记密码请求
 * 该函数会获取用户输入的邮箱地址，然后将其发送到服务器端的 forgot-password 路由
 * 服务器端会根据邮箱地址进行相应的处理，并返回处理结果
 * 函数会根据服务器返回的结果显示相应的提示信息
 */
async function submitRequest() {
    // 获取用户输入的邮箱地址
    const email = document.getElementById('email').value;
    // 如果没有输入邮箱地址，弹出提示
    if (!email) {
        alert("请输入邮箱地址！");
        return;
    }

    // 发起 AJAX 请求
    const response = await fetch('forgot-password', {
        // 请求方法为 POST
        method: 'POST',
        // 请求头，指定数据格式为表单 URL 编码
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        // 请求体，包含邮箱地址
        body: `email=${encodeURIComponent(email)}`
    });

    // 解析服务器返回的 JSON 数据
    const result = await response.json();
    // 在页面上显示服务器返回的提示信息
    document.getElementById('message').innerText = result.message;
}