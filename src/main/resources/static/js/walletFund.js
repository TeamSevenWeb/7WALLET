
// Add an event listener to the button
document.getElementById('transferForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent default button click behavior

    // Simulate transfer (replace with actual transfer logic)
    const transferAmount = document.getElementById('amount').value;
    const cardId = document.getElementById('cardId').value;

    // Simulate API request to perform transfer
    fetch('/api/wallet/fund', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authentication' : username+" "+password
        },
        body: JSON.stringify( {
            cardId : cardId,
            amount : transferAmount
        })
    }).then(response => {
        if (response.ok) {
            // Show success flyout
            alert('Transfer successful!');

            // showSuccessFlyout();
            // Hide success flyout after a certain time (optional)

            // setTimeout(hideSuccessFlyout, 5000); // Hide after 5 seconds
        } else {
            alert('Transfer failed. Please try again.');
        }
    }).catch(error => {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    });
});
