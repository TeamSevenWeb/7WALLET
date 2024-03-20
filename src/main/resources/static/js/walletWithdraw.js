function handleKeypress(event) {
    if (event.keyCode === 13) { // Check if the pressed key is Enter
        event.preventDefault(); // Prevent default behavior (form submission)
    }
}

// Function to validate the transfer amount and toggle the button class
function toggleButtonState() {
    const withdrawButton = document.getElementById('withdrawButton');
    const transferAmount = document.getElementById('amount').value;
    if (isNaN(transferAmount) || transferAmount <= 0 || !/^[1-9]\d*$/.test(transferAmount.trim())) {
        // If invalid, add the disabled class to the button
        withdrawButton.classList.add('disabled-button');

    } else {
        // If valid, remove the disabled class from the button
        withdrawButton.classList.remove('disabled-button');
    }
}


function verifyAmount() {
    const amountInput = document.getElementById('amount');
    var invalidAmountError = document.getElementById("invalidAmountError");

    if (parseFloat(amountInput.value) <= 0 || (!/^[1-9]\d*$/.test(amountInput.value.trim())
    && amountInput.value!=="")) {
        invalidAmountError.style.display = 'inline';
    } else {
        invalidAmountError.style.display = 'none';
    }
}

// Attach the verifyAmount function to the input event of the amount input field
const amountInput = document.getElementById('amount');
amountInput.addEventListener('input', verifyAmount);
amountInput.addEventListener('change', verifyAmount);
// Add event listener to the input field to trigger validation on change
amountInput.addEventListener('input', toggleButtonState);
amountInput.addEventListener('keypress', handleKeypress);
// Initial validation
toggleButtonState();
document.getElementById('transferForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent default button click behavior

    // Simulate transfer (replace with actual transfer logic)
    const transferAmount = document.getElementById('amount').value.trim();

    const cardId = document.getElementById('cardId').value;
    const loader = document.getElementById('loader');
    loader.style.display = 'inline-block';

    alert('Are you sure you want to withdraw this amount?');


    // Simulate API request to perform transfer
        fetch('/api/wallet/withdraw', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authentication': username + " " + password
            },
            body: JSON.stringify({
                cardId: cardId,
                amount: transferAmount
            })
        }).then(response => {
            if (response.ok) {
                loader.style.display = 'none';
                // Show success flyout
                alert('Transfer successful!');

                // showSuccessFlyout();
                // Hide success flyout after a certain time (optional)

                // setTimeout(hideSuccessFlyout, 5000); // Hide after 5 seconds
            } else {
                loader.style.display = 'none';
                alert('Transfer failed. Please try again.');
            }
        }).catch(error => {
            loader.style.display = 'none';
            console.error('Error:', error);
            alert('An unexpected error occurred. Please try again later.');
        });
});
