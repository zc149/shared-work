let stompClient = null;

function updateChatBox(message, isSender) {
    const chatBox = document.querySelector('.chat-box');

    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${isSender ? 'receiver' : 'sender'}`;

    messageDiv.innerHTML = `
        <div class="message-header">
            <div class="name-box">${message.writer}</div>
            <div class="date-box">${new Date(message.date).toLocaleString()}</div>
        </div>
        <div class="message-content">${message.message}</div>
    `;

    chatBox.appendChild(messageDiv);
    chatBox.scrollTop = chatBox.scrollHeight; // 최신 메시지로 스크롤
}

function loadChatMessages() {
    const chattingRoomId = 1; // 해당 채팅방 ID
    fetch(`/chatting/${chattingRoomId}/messages`)
        .then(response => response.json())
        .then(messages => {
            messages.forEach(message => {
                const isSender = message.userId == document.getElementById('userId').textContent;
                updateChatBox(message, isSender); // 각 메시지를 채팅박스에 추가
            });
        })
        .catch(error => console.error('Error fetching messages:', error));
}

function sendMessage() {
    const input = document.getElementById('messageInput');
    const userID = document.getElementById('userId').textContent;
    const userNickName = document.getElementById('userNickName').textContent;
    const messageText = input.value;

    if (messageText) {
        // 줄바꿈을 <br> 태그로 변환
        const formattedMessage = messageText.replace(/\n/g, '<br>');

        const message = {
            userId: userID,
            writer: userNickName,
            message: formattedMessage, // 변환된 메시지 사용
            date: new Date() // 현재 날짜 추가
        };
        stompClient.send('/publication/chatting/1', {}, JSON.stringify(message)); // chattingRoomId를 1로 설정
        input.value = ''; // 입력 필드 초기화
    }
}


document.addEventListener('DOMContentLoaded', (event) => {
    const socket = new SockJS('/chatting'); // WebSocket 엔드포인트
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        // /subscription/chatting/1 주제에 구독
        stompClient.subscribe('/subscription/chatting/1', function(message) {
            const data = JSON.parse(message.body);
            const isSender = data.userId == document.getElementById('userId').textContent;
            loadChatMessages();
            updateChatBox(data, isSender); // 새로운 메시지를 채팅박스에 추가
        });

        loadChatMessages(); // 페이지 로드 시 기존 메시지 불러오기
    }, function(error) {
        console.error('Connection error:', error);
    });

    document.getElementById('sendButton').addEventListener('click', sendMessage);

    document.getElementById('messageInput').addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            if (event.shiftKey) {
                return;
            } else {
                event.preventDefault();
                sendMessage();
            }
        }
    });

});
