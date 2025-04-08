const stripe = stripe('pk_test_51Psg3ZBFrizAwYCzOx5Eb3YiVZMXvQYeDsUbz0vRM16N4CWS5TO8CJuuGrprlxTxLxAzu8WqhMHxROuokI7CUaiy00cktOEU70pk_test_51Psg3ZBFrizAwYCzI6ivnDhlxkaatfA6yC4nJbIG3acGweSB7uXgCa2woQNEHbpHniO8D6YWlGdA1sigQXTP3YKp009D9oJkR3');
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });