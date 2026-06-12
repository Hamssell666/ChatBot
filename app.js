const chatForm = document.getElementById('chatForm');
const userInput = document.getElementById('userInput');
const chatBox = document.getElementById('chatBox');

chatForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const mensaje = userInput.value.trim();
    if (!mensaje) return;
    procesarPregunta(mensaje);
    userInput.value = '';
});

function enviarSugerencia(textoPregunta) {
    procesarPregunta(textoPregunta);
}

async function procesarPregunta(mensajeUsuario) {
    
    agregarMensajeAlChat(mensajeUsuario, 'user-message');
    
   
    const indicadorEscribiendo = agregarMensajeAlChat('Generando respuesta...', 'bot-message');
    chatBox.scrollTop = chatBox.scrollHeight;

    try {
        
        const response = await fetch('http://localhost:8080/api/chat/ask', {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ message: mensajeUsuario })
        });

        if (!response.ok) {
            throw new Error('Error en el servidor');
        }

        const datos = await response.json();
        
       
        indicadorEscribiendo.innerHTML = datos.response;

    } catch (error) {
        
        indicadorEscribiendo.innerHTML = `<span style="color: #dc3545;"><i class="fa-solid fa-triangle-exclamation"></i> Error: No se pudo conectar con el backend de Java.</span>`;
        console.error("Detalle del error:", error);
    }
    
    
    chatBox.scrollTop = chatBox.scrollHeight;
}

function agregarMensajeAlChat(texto, claseEstilo) {
    const elementoMensaje = document.createElement('div');
    elementoMensaje.classList.add('message', claseEstilo);
    elementoMensaje.innerHTML = texto;
    chatBox.appendChild(elementoMensaje);
    return elementoMensaje;
}