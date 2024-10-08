import { sendDate, updateTodoList } from "./main.js";

// 버튼 클릭 시 모달 열기
document.getElementById('addButton').addEventListener('click', function () {
    document.getElementById('todoModal').style.display = 'block';
});

// 추가 버튼 클릭 시 할 일 추가
document.getElementById('addTodoButton').addEventListener('click', function () {
    const content = document.getElementById('todoInput').value;
    let todoDate = document.getElementById('result').value;

    // 처음 렌더링시 날짜를 저장이 안되서 null 오류를 해결하기 위함
    if (!todoDate) {
        const today = new Date();
        todoDate = today.toISOString().split('T')[0];
    }

    if (content) {

        const todoData = { content: content, todoDate: todoDate };

        fetch('/todolist/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(todoData)
        })
            .then(response => response.json())
            .then(data => {
                sendDate(todoDate);
            })
            .catch((error) => {
                console.log('Error', error);
            })

        document.getElementById('todoInput').value = ''; // 입력 필드 초기화
        document.getElementById('todoModal').style.display = 'none'; // 모달 닫기

    } else {
        alert('할 일을 입력해 주세요.');
    }
});

// 모달 닫기 함수
function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

document.getElementById('cancelButton').addEventListener('click', function () {
    closeModal('todoModal');
});

document.getElementById('updateCancelButton').addEventListener('click', function () {
    closeModal('updateModal');
});

// 모달 외부 클릭 시 닫기
window.onclick = function (event) {
    const todoModal = document.getElementById('todoModal');
    const updateModal = document.getElementById('updateModal');

    if (event.target === todoModal) {
        closeModal('todoModal');
    }

    if (event.target === updateModal) {
        closeModal('updateModal');
    }
};
