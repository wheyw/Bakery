// Получаем id из адреса страницы
const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get('id');

// Функция для отправки запроса к API и отображения данных о медикаменте
async function getAidDetails() {
    try {
        const response = await fetch(`http://localhost:8080/api/bakery/${id}`);
        const data = await response.json();

        // Отображаем данные о медикаменте на странице
        document.getElementById('aid_image').src = data.imageURL;
        document.getElementById('medicine-name').textContent = data.name;
        document.getElementById('manufacturer').textContent = `${data.manufacturer}`;
        document.getElementById('description').textContent = `${data.description}`;
        if(data.discountPercent != 0)
            document.getElementById('old_price').textContent = `${data.price} руб.\t`;
        document.getElementById('price').textContent = `${data.price * (1-(data.discountPercent / 100))} руб.`;
        document.getElementById('add-to-cart').innerHTML=`
                <button class="add-to-cart-button" onclick="addToCart(${id});">Добавить в корзину</button>
                <lord-icon
                    onclick="addToFav(${id});"
                    src="https://cdn.lordicon.com/ulnswmkk.json"
                    trigger="click"
                    state="morph-heart"
                    colors="primary:#bb0f0f"
                    style="width:40px;height:40px;padding-top: 12px;">
                </lord-icon>
        `;
    } catch (error) {
        console.error('Ошибка при получении данных о медикаменте:', error);
    }
}
function addToCart(aidId) {
    if(!getCookie("jwt"))
        return alert('Авторизация не выполнена');
    // Формируем JSON объект для отправки на сервер
    var cartItem = {
        user_id:null,
        aid_id: aidId,
        quantity: document.getElementById('quantity').value
    };
    
    // Отправляем POST запрос на сервер
    fetch(`http://localhost:8080/api/cart/add?jwt=${getCookie("jwt")}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cartItem)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сервера: ' + response.status);
        }
        // Дополнительные действия после успешной регистрации
    })
    .catch(error => {
        alert('Ошибка при добавлении в корзину: ' + error.message);
    });
}

function addToFav(aidId) {
    if(!getCookie("jwt"))
        return alert('Авторизация не выполнена');

    var favItem = {
        user_id:null,
        aid_id: aidId
    };
    
    // Отправляем POST запрос на сервер
    fetch(`http://localhost:8080/api/users/favorite/add?jwt=${getCookie("jwt")}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(favItem)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сервера: ' + response.status);
        }
        // Дополнительные действия после успешной регистрации
    })
    .catch(error => {
        alert('Ошибка при добавлении в избранное: ' + error.message);
    });
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

// Вызываем функцию для получения данных о медикаменте
getAidDetails();
