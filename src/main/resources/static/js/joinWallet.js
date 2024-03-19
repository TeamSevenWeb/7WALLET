var closeModalBtn = document.getElementsByClassName("close");

var walletModalOverlay = document.getElementById("walletModalOverlay");

var walletFlyout = document.getElementById('walletFlyout');

function showFlyout() {
    walletModalOverlay.style.display = 'block';
    walletFlyout.style.display = "block"; // Show the overlay
}

walletModalOverlay.addEventListener("click", closeModal);
for (var i = 0; i < closeModalBtn.length; i++) {
    closeModalBtn[i].addEventListener("click", closeModal);
}

function closeModal() {
    walletFlyout.style.display = "none"; // Hide the modal
    walletModalOverlay.style.display = "none"; // Hide the overlay
}

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
            document.getElementById('walletFlyout').style.display = 'none';
            location.reload();
        } else {
            alert('Error creating wallet. Wallet name should be between 2 and 10 characters. All your wallets name should be unique');
        }
    }).catch(error => {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    });
});