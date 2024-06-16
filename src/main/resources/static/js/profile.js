document.addEventListener('DOMContentLoaded', function() {
    // Проверяем наличие JWT токена в cookie
    const jwtToken = getCookieJwt('jwt');
    getProfileData(jwtToken);

    // Получаем элементы для кнопок
    document.getElementById('logout').addEventListener('click', function() {
        if (jwtToken) {
            if(confirm("Вы уверены, что хотите выйти из аккаунта?"))
            {
                setCookie('jwt',null,0);
                window.location.href = window.location.href.replace('/profile','/auth');
            }
        } 
        else 
        {
            alert("Невозможно выйти: Авторизация не выполнена!");
        }
    })
    document.getElementById('save').addEventListener('click', function(){
        if(confirm("Сохранить изменения?"))
            updateData(jwtToken);
    } )
    document.getElementById('reset_password').addEventListener('click', function(){
        var current = prompt('Введите текущий пароль:').trim();
        var new1 = prompt('Введите новый пароль:').trim();
        var new2 = prompt('Повторите новый пароль:').trim();

        if(new1 !== new2){
            alert('Пароли не совпадают!');
            return;
        } else
        if(new1.length >= 5 && new1.length <= 20)
            {
                resetPassword(jwtToken, current, new1);
            }
    } )
});

function isValidEmail(email) {
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Функция для получения cookie по имени
function getCookieJwt(name) {
    const cookieArr = document.cookie.split(';');
    for (let i = 0; i < cookieArr.length; i++) {
        const cookiePair = cookieArr[i].split('=');
        if (name === cookiePair[0].trim()) {
            return decodeURIComponent(cookiePair[1]);
        }
    }
    return null;
}
async function getProfileData(jwt){
    try {
        const response = await fetch(`http://localhost:8080/api/users/data?jwt=${jwt}`);
        const data = await response.json();

        // Отображаем данные о медикаменте на странице
        document.getElementById('email').value = data.email;
        document.getElementById('name').value = `${data.name}`;
        document.getElementById('address').value = `${data.address}`;
    } catch (error) {
        console.error('Ошибка при получении данных о медикаменте:', error);
    }
}
function setCookie(name, value, days) {
    const expires = new Date();
    expires.setTime(expires.getTime() + days * 24 * 60 * 60 * 1000);
    document.cookie = name + '=' + encodeURIComponent(value) + ';expires=' + expires.toUTCString();
}
function updateData(jwt) {
    // Получаем значения полей формы
    var email = document.getElementById('email').value.trim();
    var name = document.getElementById('name').value.trim();
    var address = document.getElementById('address').value.trim();
    
    // Проверяем, чтобы все поля были заполнены
    if (email === '' || name === '' || address === '') {
        alert('Пожалуйста, заполните все поля');
        return;
    }
    
    // Проверяем корректность электронной почты
    if (!isValidEmail(email)) {
        alert('Пожалуйста, введите корректный email');
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
        address: address
    };
    
    // Отправляем POST запрос на сервер
    fetch(`http://localhost:8080/api/users/update?jwt=${jwt}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сервера: ' + response.status);
        }
        alert("Данные обновлены!");
    })
    .catch(error => {
        alert('Ошибка при обновлении данных: ' + error.message);
    });
    }

function resetPassword(jwt, current, new_) {
        fetch(`http://localhost:8080/api/users/update/password?jwt=${jwt}&current=${current}&new_=${new_}`, {
            method: 'PATCH'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Пароль указан неверно!');
            }
            alert("Пароль успешно изменен!");
        })
        .catch(error => {
            alert('Ошибка при обновлении данных: ' + error.message);
        });
    }

if(!getCookieJwt('jwt'))
    window.location.href = window.location.href.replace('/profile','');