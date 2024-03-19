var closeModalBtn = document.getElementsByClassName("close");

var currencyUpdateModalOverlay = document.getElementById("currencyUpdateModalOverlay");

var currencyUpdateFlyout = document.getElementById('currencyUpdateFlyout');

function showUpdateFlyout() {
    currencyUpdateModalOverlay.style.display = 'block';
    currencyUpdateFlyout.style.display = "block"; // Show the overlay
}

currencyUpdateModalOverlay.addEventListener("click", closeUpdateModal);
for (var i = 0; i < closeModalBtn.length; i++) {
    closeModalBtn[i].addEventListener("click", closeUpdateModal);
}

function closeUpdateModal() {
    currencyUpdateFlyout.style.display = "none"; // Hide the modal
    currencyUpdateModalOverlay.style.display = "none"; // Hide the overlay
}

document.getElementById('currencyUpdateForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form submission

    // Collect wallet details
    var currencyId = document.getElementById('currencyId').value;
    var currencyUpdateCode = document.getElementById('currencyUpdateCode').value;
    var ratingUpdate = document.getElementById('ratingUpdate').value;

    // Send request to wallet details
    // Example using fetch API
    fetch('/api/currency/' + currencyId, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authentication' : username +" "+password
        },
        body: JSON.stringify({
            currencyCode: currencyUpdateCode,
            rating: ratingUpdate
        })
    }).then(response => {
        if (response.ok) {
            alert('Currency updated successfully!');
            // Close the flyout after saving
            document.getElementById('currencyUpdateFlyout').style.display = 'none';
            location.reload();
        } else {
            alert('Error updating currency. Currency code should be 3 characters. All currencies code should be unique.' +
                'Choose existing currency.');
        }
    }).catch(error => {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    });
});