document.getElementById('walletForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form submission

    // Collect wallet details
    var walletName = document.getElementById('name').value;

    // Send request to wallet details
    // Example using fetch API
    fetch('/api/join wallet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authentication' : username +" "+password
        },
        body: JSON.stringify({
            name: walletName
        })
    }).then(response => {
        if (response.ok) {
            alert('Wallet created successfully!');
            // Close the flyout after saving
            document.getElementById('cardFlyout').style.display = 'none';
            location.reload();
        } else {
            alert('Error creating wallet. Please check your details and try again.');
        }
    }).catch(error => {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    });
});