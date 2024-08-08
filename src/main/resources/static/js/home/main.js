$(function () {

    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0]; // YYYY-MM-DD 형식으로 변환

    sendDate(formattedDate); // 처음 렌더링시 오늘 날짜

    rome(inline_cal, { time: false, inputFormat: 'YYYY-MM-DD' }).on('data', function (value) {
        result.value = value;
        sendDate(value);
    });
});

function sendDate(date) {
    $.ajax({
        url: '/todolist',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ todoDate: date }),
        success: function (response) {
            updateTodoList(response);  // 받은 데이터를 화면에 반영
        },
        error: function (xhr, status, error) {
                            console.error('실패:', error);
        }
    });
}

function updateTodoList(todoList) {
    var tbody = $('.table tbody');
    tbody.empty();  // 기존 데이터 비우기
    if (todoList.length === 0) {
        tbody.append('<tr><td>목록이 없습니다.</td></tr>');  // 비어있을 경우 메시지
    } else {
        todoList.forEach(function (todo) {
            const isChecked = todo.status === "true";  // 상태에 따라 체크 여부 결정
            tbody.append('<tr>' +
                '<td style="display: flex; align-items: center;">' +
                '<div class="icon-container" style="margin-right: 8px; cursor: pointer;">' +
                    '<svg class="icon unchecked" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16" style="display: ' + (isChecked ? 'none' : 'block') + ';">' +
                        '<path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z"/>' +
                        '<path d="M10.97 4.97a.75.75 0 0 1 1.071 1.05l-3.992 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z"/>' +
                    '</svg>' +
                    '<svg class="icon checked" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16" style="display: ' + (isChecked ? 'block' : 'none') + ';">' +
                        '<path d="M2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zm10.03 4.97a.75.75 0 0 1 .011 1.05l-3.992 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425a.75.75 0 0 1 1.08-.022z"/>' +
                    '</svg>' +
                '</div>' +
                '<span data-id="' + todo.todoId + '" style="flex-grow: 1; text-decoration: ' + (isChecked ? 'line-through' : 'none') + ';">' + todo.content + '</span>' +
                '<div style="display: flex; gap: 8px;">' +
                '<svg class="updateIcon" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil" viewBox="0 0 16 16" style="cursor: pointer;">' +
                '<path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325"/>' +
                '</svg>' +
                '<svg class="deleteIcon" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16" style="cursor: pointer;">' +
                '<path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z"/>' +
                '<path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z"/>' +
                '</svg>' +
                '</div>' +
                '</td>' +
                '</tr>');
        });

    }
    addDeleteEventListeners();
    updateModalEventListeners(todoList);
}


// 삭제 아이콘 클릭 이벤트
function addDeleteEventListeners() {
    document.querySelectorAll('.deleteIcon').forEach(icon => {
        icon.addEventListener('click', function () {
            const todoId = this.closest('td').querySelector('span').getAttribute('data-id');
            const todoData = { todoId: todoId };

            $.ajax({
                url: '/todolist/delete',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(todoData),
                success: function (response) {
                    const todoDate = document.getElementById('result').value;
                    sendDate(todoDate);
                },
                error: function (xhr, status, error) {
                    console.error('실패:', error);
                }
            });
        });
    });
}

// 수정 아이콘 클릭 이벤트
function updateModalEventListeners(todoList) {
    document.querySelectorAll('.updateIcon').forEach(icon => {
        icon.addEventListener('click', function () {
            const todoId = this.closest('td').querySelector('span').getAttribute('data-id');
            const todoItem = todoList.find(todo => todo.todoId === Number(todoId));
            if (todoItem) {
                const beforeContent = todoItem.content;
                document.getElementById('updateInput').value = beforeContent;
                document.getElementById('updateModal').style.display = 'block';

                // 모달의 저장 버튼 클릭 이벤트 설정
                document.getElementById('updateButton').onclick = function () {
                    const content = document.getElementById('updateInput').value;
                    const todoDate = document.getElementById('result').value;
                    const todoData = { todoId: todoId, content: content, todoDate: todoDate, status: todoItem.status};

                    updateEventListener(todoData);
                };
            }
        });
    });
}

function updateEventListener(todoData) {
    $.ajax({
        url: '/todolist/update',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(todoData),
        success: function (response) {
            sendDate(todoData.todoDate);
            document.getElementById('updateModal').style.display = 'none'; // 모달 닫기
        },
        error: function (xhr, status, error) {
            console.error('실패:', error);
        }
    });
}

function checkEventListener(todoData) {
    $.ajax({
        url: '/todolist/check',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(todoData),
        success: function (response) {
        },
        error: function (xhr, status, error) {
            console.error('실패:', error);
        }
    });
}

// 체크박스 아이콘 클릭 이벤트 리스너 추가
$(document).on('click', '.icon-container', function() {
    const todoId = $(this).siblings('span[data-id]').data('id');
    const uncheckedIcon = $(this).find('.unchecked');
    const checkedIcon = $(this).find('.checked');
    const span = $('span[data-id="' + todoId + '"]');

    let todoData = { status: false };

    if (uncheckedIcon.is(':visible')) {
        span.css('text-decoration', 'line-through'); // 체크된 경우
        uncheckedIcon.hide();
        checkedIcon.show();
        todoData.status = true;
    } else {
        span.css('text-decoration', 'none'); // 체크 해제된 경우
        uncheckedIcon.show();
        checkedIcon.hide();
        todoData.status = false;
    }

    todoData.todoId = todoId;
    checkEventListener(todoData);
});


export { sendDate, updateTodoList };
