const urlParams = new URLSearchParams(window.location.search);
    const _page = urlParams.get('page') || 0;
    const _search = urlParams.get('search') || '';
    const pageSize = 15;

    // Функция для загрузки истории покупок
    function loadHistory(page) {
        const url = `http://localhost:8080/api/users/history?page=${page}&size=${pageSize}&search=${_search}&jwt=${getCookie("jwt")}`;
        fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Ошибка HTTP: ' + response.status);
                    }
                    return response.json(); // Преобразование ответа в JSON
                })
                .then(cartItems => {
                    const aidsContainer = document.getElementById('history-container');
                    // Очистка контейнера перед добавлением новых товаров
                    aidsContainer.innerHTML = '';

                    // Проход по каждому товару в корзине и создание карточки
                    cartItems.forEach(cartItem => {
                        fetch(`http://localhost:8080/api/bakery/${cartItem.aid_id}`)
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Ошибка HTTP: ' + response.status);
                                }
                                return response.json(); // Преобразование ответа в JSON
                            })
                            .then(aid => {
                                const aidCard = document.createElement('div');
                                aidCard.classList.add('product-card');
                                aidCard.id = `item-${cartItem.id}`;

                                // Заполнение информации о товаре
                                aidCard.innerHTML = `
                                <a href='bakery?id=${aid.id}' class="product-image">
                                    <img src="${aid.imageURL}" alt="Картинка">
                                </a>
                                <div class="product-info">
                                    <h2 class="product-name">${aid.name}</h2>
                                    <h2 class="delivery-status">Получен</h2>
                                    <p class="purchase-date">Количество: ${cartItem.quantity}</p>
                                    <p class="purchase-date">Сумма: ${cartItem.quantity*cartItem.price} ₽</p>
                                    <p class="purchase-date">Дата покупки: ${cartItem.boughtAt.split('T')[0]}</p>
                                    <p class="delivery-date">Дата доставки: ${cartItem.deliveryDate.split('T')[0]}</p>
                                </div>
                                `;

                                // Добавление карточки товара в контейнер
                                aidsContainer.appendChild(aidCard);
                            })
                            .catch(error => console.error('Ошибка загрузки заказа:', error));
                    });
                })
                .catch(error => console.error('Ошибка загрузки заказа:', error));
    }
    function loadActive() {
        const url = `http://localhost:8080/api/users/history/active?jwt=${getCookie("jwt")}`;
        fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Ошибка HTTP: ' + response.status);
                    }
                    return response.json(); // Преобразование ответа в JSON
                })
                .then(cartItems => {
                    const aidsContainer = document.getElementById('current-container');
                    // Очистка контейнера перед добавлением новых товаров
                    aidsContainer.innerHTML = '';

                    // Проход по каждому товару в корзине и создание карточки
                    cartItems.forEach(cartItem => {
                        fetch(`http://localhost:8080/api/bakery/${cartItem.aid_id}`)
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Ошибка HTTP: ' + response.status);
                                }
                                return response.json(); // Преобразование ответа в JSON
                            })
                            .then(aid => {
                                const aidCard = document.createElement('div');
                                aidCard.classList.add('product-card');
                                aidCard.id = `item-${cartItem.id}`;

                                // Заполнение информации о товаре
                                aidCard.innerHTML = `
                                <a href='bakery?id=${aid.id}' class="product-image">
                                    <img src="${aid.imageURL}" alt="Картинка">
                                </a>
                                <div class="product-info">
                                    <h2 class="product-name">${aid.name}</h2>
                                    <h2 class="delivery-status">В процессе доставки</h2>
                                    <p class="purchase-date">Количество: ${cartItem.quantity}</p>
                                    <p class="purchase-date">Сумма: ${cartItem.quantity*cartItem.price} ₽</p>
                                    <p class="purchase-date">Дата покупки: ${cartItem.boughtAt.split('T')[0]}</p>
                                    <p class="delivery-date">Дата доставки: ${cartItem.deliveryDate.split('T')[0]}</p>
                                    <button onClick='confirmItem(${cartItem.id});'>Подтвердить получение</button>
                                </div>
                                `;

                                // Добавление карточки товара в контейнер
                                aidsContainer.appendChild(aidCard);
                            })
                            .catch(error => console.error('Ошибка загрузки заказа:', error));
                    });
                })
                .catch(error => console.error('Ошибка загрузки заказа:', error));
    }
    function loadPages() {
        // Запрос на получение данных о пользователях
        const url2 = `http://localhost:8080/api/users/history/count?search=${_search}&jwt=${getCookie("jwt")}`;
        fetch(url2)
        .then(response => response.json())
        .then(data => {
            const numberData = Number(data); // Преобразование данных в число
            const container = document.getElementById('pages-container');
            container.innerHTML = '';

            if(numberData > pageSize){
            // Цикл для создания ссылок на страницы
            for (let page = 0; page < numberData/pageSize; page++) {
                        const link = document.createElement('a');
                        link.href = `?page=${page}&search=${_search}`;
                        link.textContent = `${page+1}`;
                        link.className = 'pageLink';
                        if(page ==_page)
                            link.style = 'background-color: #054e23;';

                        // Добавление ссылки в контейнер
                        container.appendChild(link);
                    }
                }
            })
            .catch(error => console.error('Ошибка:', error));
        }
    // Функция для перехода к профилю выбранного пользователя
    function viewAid(aidId) {
        window.location.href = `http://localhost:8080/api/bakery/${aidId}`;
    }
    function applyFilter(){
            const search_val = document.getElementById("search_input").value;
            window.location.href = `http://localhost:8080/history?search=${search_val}`;
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
        document.addEventListener("DOMContentLoaded", function() {
            if(!getCookie('jwt'))
                window.location.href = window.location.href.replace('/history','');
            const searchInput = document.getElementById("search_input");
            loadHistory(_page);
            loadActive();
            loadPages();
            if (searchInput && _search) {
                searchInput.value = _search;
            }
        });

        function confirmItem(id) {
            if(confirm('Подтвердите получение заказа (это действие нельзя будет отменить)')){
            fetch(`http://localhost:8080/api/users/history/confirm?jwt=${getCookie("jwt")}&item=${id}`, {
                method: 'PATCH'
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Ошибка сервера: ' + response.status);
                }
                document.getElementById(`item-${id}`).style.display = 'none';
                loadHistory(_page);
                loadPages();
            })
            .catch(error => {
                alert('Ошибка при удалении: ' + error.message);
            });
        }
        }

