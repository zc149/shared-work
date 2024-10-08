document.addEventListener('DOMContentLoaded', () => {
    let isAuthVerified = false;
    let passwordCheck = false;

    function isValidPassword(password) {
        const minLength = 8;
        const hasEnglish = /[a-zA-Z]/.test(password);
        const hasNumber = /\d/.test(password);
        const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password); // 특수문자 포함 여부

        return password.length >= minLength && hasEnglish && hasNumber && hasSpecialChar;
    }

    const passwordMessage = document.getElementById('password-message');

    function checkPasswordMatch() {
        const password = document.getElementById('password').value;
        const password2 = document.getElementById('password2').value;

        if (isValidPassword(password) && password === password2 && password2 !== '') {
            passwordMessage.style.display = 'block';
            passwordMessage.textContent = "비밀번호가 일치합니다.";
            passwordCheck = true;
        } else {
            passwordMessage.style.display = 'block';
            passwordMessage.textContent = "비밀번호가 일치하지 않습니다.";
            passwordCheck = false;
        }

        validateButton();
    }

    document.getElementById('password').addEventListener('input', checkPasswordMatch);
    document.getElementById('password2').addEventListener('input', checkPasswordMatch);

    function validateButton() {
        const submitButton = document.getElementById('submit-button');
        const name = document.getElementById('name').value;
        const nickname = document.getElementById('nickname').value;

        if (isAuthVerified && passwordCheck && name && nickname) {
            submitButton.disabled = false; // 버튼 활성화
        } else {
            submitButton.disabled = true; // 버튼 비활성화
        }
    }

    document.getElementById('name').addEventListener('input', validateButton);
    document.getElementById('nickname').addEventListener('input', validateButton);

    const emailInput = document.getElementById('email');
    const verificationSection = document.getElementById('verification-section');
    const requestVerificationButton = document.getElementById('request-verification');

    emailInput.addEventListener('input', () => {
        const email = emailInput.value;
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (emailPattern.test(email)) {
            verificationSection.classList.remove('hidden');
            requestVerificationButton.disabled = false;
        } else {
            verificationSection.classList.add('hidden');
            requestVerificationButton.disabled = true;
        }
    });

    requestVerificationButton.addEventListener('click', async () => {
        const email = emailInput.value;
        const response = await fetch('/mail/send-certification', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email: email })
        });

        if (response.ok) {
            alert('인증번호가 전송되었습니다.');
        } else {
            alert('인증번호 전송에 실패했습니다. 다시 시도해주세요.');
        }
    });

    const authNumInput = document.getElementById('verification-code');
    const verificationMessage = document.getElementById('verification-message');

    authNumInput.addEventListener('input', async () => {
        if (authNumInput.value.length === 6) {
            const authNum = authNumInput.value;
            const email = emailInput.value;
            const response = await fetch('/mail/authCheck', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ authNum: authNum, email: email })
            });

            const responseData = await response.text();

            if (responseData === 'ok') {
                verificationMessage.style.display = 'block';
                isAuthVerified = true;
            } else {
                alert('인증번호가 일치하지 않습니다. 다시 확인해주세요.');
                verificationMessage.style.display = 'none';
                isAuthVerified = false;
            }
            validateButton();
        } else {
            verificationMessage.style.display = 'none';
            isAuthVerified = false;
            validateButton();
        }
    });
});