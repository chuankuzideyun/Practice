require("dotenv").config();
const chatInput = document.querySelector(".chat-input textarea");
const sendChatBtn = document.querySelector(".chat-input span");
const chatbox = document.querySelector(".chatbox");

let userMessage;
const API_KEY = process.env.API_KEY;

const createChatLi = (message, className)=>{
  const chatLi = document.createElement("li");
  chatLi.classList.add("chat", className);
  let chatContent = className === "outgoing"? `<p>${message}</p>`: `<span class="material-symbols-outlined">smart_toy</span><p>${message}</p>`;
  chatLi.innerHTML = chatContent;
  return chatLi;
}

const generateResponse =(incomingChatLi)=>{
  const API_URL = "https://api.openai.com/v1/chat/completions";
  const messageElement = incomingChatLi;
  const requestOptions ={
    method: "POST",
    headers:{
      "Content-Type": "application/json",
      "Authorization": `Bearer ${API_KEY}`
    },
    body: JSON.stringify({
      "model": "gpt-3.5-turbo",
    "messages": [
      {
        "role": "user",
        "content": userMessage
      }
    ]
    })
  }
  fetch(API_URL, requestOptions).then(res=> res.json()).then(data=>{
    messageElement.textContent = data.choices[0].message.content;
  }).catch((error)=>{
    messageElement.textContent = "Oops! Something went wrong. Please try again.";
  }).finally(()=> chatbox?.scrollTo(0, chatbox.scrollHeight));
}
const handleChat = ()=>{
  userMessage = chatInput.value.trim();
  if(!userMessage) return;
  chatbox.appendChild(createChatLi(userMessage, "outgoing"));
  chatbox?.scrollTo(0, chatbox.scrollHeight);

  setTimeout(()=>{
    const incomingChatLi = createChatLi("Thinking...","incoming");
    chatbox?.appendChild(incomingChatLi);
    chatbox?.scrollTo(0, chatbox.scrollHeight);
    generateResponse(incomingChatLi);
  },600)
}
sendChatBtn?.addEventListener("click",handleChat);