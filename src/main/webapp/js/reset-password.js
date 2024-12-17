/**
 * 重置用户密码
 * 该函数会从 URL 中获取重置密码的 token，然后检查用户输入的新密码和确认密码是否一致
 * 如果一致，它会将 token 和新密码发送到服务器端的 reset-password 路由进行密码重置
 * 服务器端会根据 token 和新密码进行相应的处理，并返回处理结果
 * 函数会根据服务器返回的结果显示相应的提示信息
 */
async function resetPassword() {
    // 从 URL 中获取重置密码的 token
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    // 获取用户输入的新密码
    const password = document.getElementById('password').value;
    // 获取用户输入的确认密码
    const confirmPassword = document.getElementById('confirmPassword').value;
    // 检查是否输入了密码
    if (!password || !confirmPassword) {
        // 如果没有输入密码，弹出提示
        alert("请输入密码！");
        return;
    }
    // 检查两次输入的密码是否一致
    if (password !== confirmPassword) {
        // 如果两次输入的密码不一致，弹出提示
        alert("两次输入的密码不一致！");
        return;
    }

    // 发起 AJAX 请求
    const response = await fetch('reset-password', {
        // 请求方法为 POST
        method: 'POST',
        // 请求头，指定数据格式为表单 URL 编码
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        // 请求体，包含 token 和新密码
        body: `token=${encodeURIComponent(token)}&password=${encodeURIComponent(password)}`
    });
    // 解析服务器返回的 JSON 数据
    const result = await response.json();
    // 在页面上显示服务器返回的提示信息
    document.getElementById('message').innerText = result.message;
}