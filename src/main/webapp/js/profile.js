// 打开模态框
/**
 * 打开模态框，允许用户编辑个人资料中的某个字段
 * @param {string} field - 要编辑的字段名称
 * @param {string} value - 字段的当前值
 */
function openModal(field, value) {
    // 显示模态框
    document.getElementById('editModal').style.display = 'flex';
    // 设置模态框标题为“编辑 [字段名]”
    document.getElementById('modalTitle').textContent = `编辑 ${field}`;
    // 在模态框中显示要编辑的字段名
    document.getElementById('field').value = field;
    // 在模态框中显示字段的当前值，如果没有提供当前值，则为空
    document.getElementById('newValue').value = value || '';

    // 获取确认区域的 DOM 元素
    const confirmationSection = document.getElementById('confirmationSection');
    // 如果要编辑的字段是密码或电话号码，则显示确认区域
    if (field === 'password' || field === 'phonenumber') {
        confirmationSection.style.display = 'block';
        // 清空确认输入框
        document.getElementById('confirmValue').value = '';
    } else {
        // 否则，隐藏确认区域
        confirmationSection.style.display = 'none';
    }
}

// 关闭模态框
function closeModal() {
    document.getElementById('editModal').style.display = 'none';
}

/**
 * 提交编辑后的个人资料字段
 * 该函数会获取用户在模态框中输入的新值和确认值（如果需要），然后通过 AJAX 请求将这些值发送到服务器端的 EditProfileServlet
 * 服务器端会根据提交的字段和值进行相应的处理，并返回处理结果
 * 函数会根据服务器返回的结果显示相应的提示信息，并在修改成功时更新页面上显示的字段值
 */
function submitEdit() {
    // 获取要编辑的字段名
    const field = document.getElementById('field').value;
    // 获取新的值
    const newValue = document.getElementById('newValue').value;
    // 获取确认值，如果不需要确认则为空
    const confirmValue = document.getElementById('confirmValue')? document.getElementById('confirmValue').value : '';

    // 检查是否需要二次确认
    if ((field === 'password' || field === 'phonenumber') &&!confirmValue) {
        // 如果需要确认但未填写确认信息，弹出提示
        alert('请填写确认信息！');
        return;
    }

    // 发起 AJAX 请求
    const xhr = new XMLHttpRequest();
    // 设置请求方法和目标 URL
    xhr.open('POST', 'EditProfileServlet', true);
    // 设置请求头，指定数据格式为表单 URL 编码
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    // 定义请求状态改变时的回调函数
    xhr.onreadystatechange = function () {
        // 检查请求是否完成且响应状态码为 200（成功）
        if (xhr.readyState === 4 && xhr.status === 200) {
            // 根据服务器返回的结果进行相应的处理
            switch (xhr.responseText) {
                case 'success':
                    // 修改成功，弹出提示并关闭模态框
                    alert('修改成功！');
                    // 如果修改的不是密码，则更新页面上显示的字段值
                    if (field!== "password") {
                        document.getElementById(`display-${field}`).innerText = newValue;
                    }
                    closeModal();
                    break;

                case 'confirm-fail':
                    // 确认失败，弹出提示
                    alert('确认失败，请重新输入密码！');
                    break;

                case 'fail':
                    // 修改失败，弹出提示
                    alert('修改失败，请重试！');
                    break;

                case 'invalid-field':
                    // 无效的字段，弹出提示
                    alert('无效的字段！');
                    break;

                default:
                    // 未知错误，弹出提示
                    alert('未知错误，请联系管理员！');
            }
        }
    };

    // 发送请求，将字段名、新值和确认值作为请求参数
    xhr.send(`field=${encodeURIComponent(field)}&newValue=${encodeURIComponent(newValue)}&confirmValue=${encodeURIComponent(confirmValue)}`);
}



window.onclick = function (event) {
    const modal = document.getElementById('editModal');
    if (event.target === modal) {
        closeModal();
    }
};