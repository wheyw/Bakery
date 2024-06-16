document.addEventListener('DOMContentLoaded', function() {
    const jwtToken = getCookie('jwt');
    if(jwtToken)
        window.location.href=window.location.href.replace('auth','profile');

    document.getElementById('reg_button').addEventListener('click', function() {
// Получаем значения полей формы
var email = document.getElementById('reg_email').value.trim();
var name = document.getElementById('reg_name').value.trim();
var password = document.getElementById('reg_password').value.trim();
var passwordRepeat = document.getElementById('reg_password_repeat').value.trim();

// Проверяем, чтобы все поля были заполнены
if (email === '' || name === '' || password === '' || passwordRepeat === '') {
    alert('Пожалуйста, заполните все поля');
    return;
}

// Проверяем совпадение паролей
if (password !== passwordRepeat) {
    alert('Пароли не совпадают');
    return;
}

// Проверяем корректность электронной почты
if (!isValidEmail(email)) {
    alert('Пожалуйста, введите корректный email');
    return;
}
// Проверяем длину пароля
if (password.length < 5 || password.length > 20) {
    alert('Пароль должен содержать от 5 до 20 символов');
    return;
}
if (name.length < 2 || name.length > 25) {
    alert('Имя должно содержать от 2 до 25 символов');
    return;
}

// Формируем JSON объект для отправки на сервер
var userData = {
    email: email,
    name: name,
    passwordHash: password
};

// Отправляем POST запрос на сервер
fetch('http://localhost:8080/api/users/add', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(userData)
})
.then(response => {
    if (!response.ok) {
        throw new Error('Ошибка сервера: ' + response.status);
    }
    alert("Аккаунт успешно создан!");
    //toLoginForm();
    // Дополнительные действия после успешной регистрации
})
.catch(error => {
    alert('Ошибка при создании аккаунта: ' + error.message);
});
});

document.getElementById('login_button').addEventListener('click', async function() {
    // Получаем значения полей формы
    var email = document.getElementById('login_email').value.trim();
    var password = document.getElementById('login_password').value.trim();
    
    // Проверяем, чтобы все поля были заполнены
    if (email === '' || password === '') {
        alert('Пожалуйста, заполните все поля');
        return;
    }
    
    // Проверяем корректность электронной почты
    if (!isValidEmail(email)) {
        alert('Пожалуйста, введите корректный email');
        return;
    }
    
    // Формируем JSON объект для отправки на сервер
    var userData = {
        email: email,
        name: 'authAction',
        passwordHash: password
    };
    
    // Отправляем POST запрос на сервер
    fetch('http://localhost:8080/api/users/auth', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(async response => {
        if (!response.ok) {
            throw new Error('Ошибка сервера: ' + response.status);
        }
        const result = await response.text();
        if(result.trim() !== 'Неверный email или пароль!'){
            setCookie('jwt',result.trim(),10);
            window.location.href = window.location.href.replace('auth','catalog');
        } else {
            alert(result);
        }
    })
    .catch(error => {
        alert('Ошибка при авторизации: ' + error.message);
    });
    });

document.getElementById('switch-to-register').addEventListener('click', function(event) {
    event.preventDefault();
    document.getElementById('register-form').style.display = 'block';
    document.getElementById('login-form').style.display = 'none';
});

// Слушатель для переключения на форму авторизации
document.getElementById('switch-to-login').addEventListener('click', function(event) {
    toLoginForm();
});
});
function setCookie(name, value, days) {
    const expires = new Date();
    expires.setTime(expires.getTime() + days * 24 * 60 * 60 * 1000);
    document.cookie = name + '=' + encodeURIComponent(value) + ';expires=' + expires.toUTCString();
}
function getCookie(name) {
    const cookieArr = document.cookie.split(';');
    for (let i = 0; i < cookieArr.length; i++) {
        const cookiePair = cookieArr[i].split('=');
        if (name === cookiePair[0].trim()) {
            return decodeURIComponent(cookiePair[1]);
        }
    }
    return null;
}
function toLoginForm(){
    event.preventDefault();
    document.getElementById('register-form').style.display = 'none';
    document.getElementById('login-form').style.display = 'block';
}

// Функция для проверки корректности email
function isValidEmail(email) {
var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
return emailRegex.test(email);
}