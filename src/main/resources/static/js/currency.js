var closeModalBtn = document.getElementsByClassName("close");

var currencyModalOverlay = document.getElementById("currencyModalOverlay");

var currencyFlyout = document.getElementById('currencyFlyout');

function showFlyout() {
    currencyModalOverlay.style.display = 'block';
    currencyFlyout.style.display = "block"; // Show the overlay
}

currencyModalOverlay.addEventListener("click", closeModal);
for (var i = 0; i < closeModalBtn.length; i++) {
    closeModalBtn[i].addEventListener("click", closeModal);
}

function closeModal() {
    currencyFlyout.style.display = "none"; // Hide the modal
    currencyModalOverlay.style.display = "none"; // Hide the overlay
}

document.getElementById('currencyForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form submission

    // Collect wallet details
    var currencyCode = document.getElementById('currencyCode').value;
    var rating = document.getElementById('rating').value;

    // Send request to wallet details
    // Example using fetch API
    fetch('/api/currency', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authentication' : username +" "+password
        },
        body: JSON.stringify({
            currencyCode: currencyCode,
            rating: rating
        })
    }).then(response => {
        if (response.ok) {
            alert('Currency created successfully!');
            // Close the flyout after saving
            document.getElementById('currencyFlyout').style.display = 'none';
            location.reload();
        } else {
            alert('Error creating currency. Currency code should be 3 characters. All currencies code should be unique');
        }
    }).catch(error => {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    });
});