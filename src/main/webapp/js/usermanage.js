/**
 * 打开模态框，允许用户编辑个人资料中的某个字段
 * @param {string} id - 用户的唯一标识符
 * @param {string} name - 用户的姓名
 * @param {string} idnumber - 用户的身份证号码
 * @param {string} phonenumber - 用户的电话号码
 * @param {string} email - 用户的电子邮件地址
 * @param {string} role - 用户的角色
 */
function openEditModal(id, name, idnumber, phonenumber, email, role) {
    // 在模态框中显示用户的唯一标识符
    document.getElementById('userId').value = id;
    // 在模态框中显示用户的姓名
    document.getElementById('userName').value = name;
    // 在模态框中显示用户的身份证号码
    document.getElementById('userIdNumber').value = idnumber;
    // 在模态框中显示用户的电话号码
    document.getElementById('userPhoneNumber').value = phonenumber;
    // 在模态框中显示用户的电子邮件地址
    document.getElementById('userEmail').value = email;
    // 在模态框中显示用户的角色
    document.getElementById('userRole').value = role;
    // 显示模态框
    document.getElementById('editModal').style.display = 'block';
}


function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
}

/**
 * 根据用户输入的搜索条件过滤表格中的数据
 * 该函数会获取用户在搜索框、角色过滤器、开始日期和结束日期中输入的值
 * 然后遍历表格中的每一行，根据这些值来决定是否显示该行
 * 如果行中的数据不满足搜索条件，则将该行隐藏
 */
function filterTable() {
    // 获取搜索框中的值，并转换为小写
    const searchInput = document.getElementById('searchInput').value.toLowerCase();
    // 获取角色过滤器中的值，并转换为小写
    const roleFilter = document.getElementById('roleFilter').value.toLowerCase();
    // 获取开始日期的值
    const startDate = document.getElementById('startDate').value;
    // 获取结束日期的值
    const endDate = document.getElementById('endDate').value;

    // 获取表格元素
    const table = document.getElementById('userTable');
    // 获取表格中的所有行
    const rows = table.getElementsByTagName('tr');

    // 遍历表格中的每一行，从第二行开始（因为第一行通常是表头）
    for (let i = 1; i < rows.length; i++) {
        // 获取当前行的所有单元格
        const cells = rows[i].getElementsByTagName('td');
        // 获取姓名单元格的文本内容，并转换为小写
        const name = cells[1].textContent.toLowerCase();
        // 获取身份证号单元格的文本内容，并转换为小写
        const idnumber = cells[2].textContent.toLowerCase();
        // 获取电话号码单元格的文本内容，并转换为小写
        const phonenumber = cells[3].textContent.toLowerCase();
        // 获取电子邮件单元格的文本内容，并转换为小写
        const email = cells[4].textContent.toLowerCase();
        // 获取角色单元格的文本内容，并转换为小写
        const role = cells[5].textContent.toLowerCase();
        // 获取创建时间单元格的文本内容
        const createTime = cells[6].textContent;

        // 初始设置当前行应该显示
        let show = true;

        // 如果搜索框中有值，并且姓名、身份证号、电话号码或电子邮件中没有包含搜索词，则隐藏该行
        if (searchInput &&![name, idnumber, phonenumber, email].some(field => field.includes(searchInput))) {
            show = false;
        }

        // 如果角色过滤器中有值，并且当前行的角色与过滤器中的角色不匹配，则隐藏该行
        if (roleFilter && role!== roleFilter) {
            show = false;
        }

        // 如果开始日期中有值，并且当前行的创建时间早于开始日期，则隐藏该行
        if (startDate && new Date(createTime) < new Date(startDate)) {
            show = false;
        }

        // 如果结束日期中有值，并且当前行的创建时间晚于结束日期，则隐藏该行
        if (endDate && new Date(createTime) > new Date(endDate)) {
            show = false;
        }

        // 根据 show 变量的值来设置当前行的显示状态
        rows[i].style.display = show? '' : 'none';
    }
}


/**
 * 确认并执行删除用户的操作
 * @param {string} userId - 要删除的用户的唯一标识符
 * 该函数会提示管理员输入密码以确认删除操作，然后使用 AJAX 发送删除请求到服务器
 * 如果服务器返回成功，则刷新页面以更新用户列表
 */
function confirmDelete(userId) {
    // 提示管理员输入密码以确认删除操作
    const adminPassword = prompt("请输入管理员密码以确认删除操作：");

    // 如果管理员取消了输入或者输入为空，则提示密码不能为空
    if (adminPassword === null || adminPassword.trim() === "") {
        alert("管理员密码不能为空！");
        return;
    }

    // 再次确认是否要删除该用户
    if (confirm("确认要删除该用户吗？")) {
        // 使用 AJAX 发送删除请求到服务器
        fetch("DeleteUserServlet", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `id=${userId}&adminPassword=${encodeURIComponent(adminPassword)}`
        })
            .then(response => response.json())
            .then(data => {
                // 如果服务器返回成功，则提示删除成功并刷新页面
                if (data.success) {
                    alert(data.message);
                    location.reload(); // 刷新页面以更新用户列表
                } else {
                    // 如果服务器返回失败，则提示失败信息
                    alert(data.message);
                }
            })
            .catch(error => {
                // 如果 AJAX 请求失败，则记录错误并提示服务器错误
                console.error("删除失败：", error);
                alert("服务器错误，请稍后重试！");
            });
    }
}

async function resetPasswordAdmin(userId) {
    try {
        const response = await fetch('ResetPasswordServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ id: userId }), // 使用 JSON 格式发送数据
        });

        if (!response.ok) {
            throw new Error('服务器错误，请稍后重试');
        }

        const result = await response.json();
        alert(result.message); // 弹窗显示结果消息
    } catch (error) {
        console.error(error);
        alert('请求失败，请稍后重试');
    }
}


function closeDeleteModal() {
    document.getElementById('deleteModal').style.display = 'none';
}