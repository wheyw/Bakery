const urlParams = new URLSearchParams(window.location.search);
    const _page = urlParams.get('page') || 0; // Если параметр не указан, используется значение по умолчанию 1
    const _search = urlParams.get('search') || '';
    const pageSize = 16;

    // Функция для загрузки данных о пользователях
    function loadFavs(page) {
        const url = `http://localhost:8080/api/users/favorite?page=${page}&size=${pageSize}&search=${_search}&jwt=${getCookie("jwt")}`;
        fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Ошибка HTTP: ' + response.status);
                    }
                    return response.json(); // Преобразование ответа в JSON
                })
                .then(cartItems => {
                    const aidsContainer = document.getElementById('aids-container');
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
                                aidCard.classList.add('medicine');
                                aidCard.id = `item-${aid.id}`;

                                // Заполнение информации о товаре
                                aidCard.innerHTML = `
                                    <a href="bakery?id=${aid.id}" style="text-decoration: none; color: #000;">
                                        <img src="${aid.imageURL}" alt="Картинка">
                                        <h3 class="medicine-name">${aid.name}</h3>
                                    </a>
                                    <a href="catalog?search=${aid.manufacturer}" style="text-decoration: none; color: #000;">
                                        <p class="medicine-description">${aid.manufacturer}</p><br/>
                                    </a>
                                    <button class="logout-button" onClick="deleteFav(${aid.id});" type="button">Удалить</button>
                                `;

                                // Добавление карточки товара в контейнер
                                aidsContainer.appendChild(aidCard);
                            })
                            .catch(error => console.error('Ошибка загрузки товара:', error));
                    });
                })
                .catch(error => console.error('Ошибка загрузки избранного:', error));
    }
    function loadPages() {
        // Запрос на получение данных о пользователях
        const url2 = `http://localhost:8080/api/users/favorite/count?search=${_search}&jwt=${getCookie("jwt")}`;
        fetch(url2)
        .then(response => response.json())
        .then(data => {
            const numberData = Number(data); // Преобразование данных в число
            const container = document.getElementById('pages-container');

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
            window.location.href = `http://localhost:8080/favorite?search=${search_val}`;
    }

    function addToCart(aidId) {
        // Получаем значения полей формы

        // Формируем JSON объект для отправки на сервер
        var cartItem = {
            user_id:null,
            aid_id: aidId,
            quantity: 1
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
            const searchInput = document.getElementById("search_input");
            loadFavs(_page);
            loadPages();
            if (searchInput && _search) {
                searchInput.value = _search;
            }
        });

        function deleteFav(aidId) {
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
                document.getElementById(`item-${aidId}`).style.display = 'none';
            })
            .catch(error => {
                alert('Ошибка при удалении: ' + error.message);
            });
        }

        if(!getCookie('jwt'))
            window.location.href = window.location.href.replace('/favorite','');