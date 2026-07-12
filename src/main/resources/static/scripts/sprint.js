const addReaderMenu = document.getElementById("addReaderMenu");

const contextPath = addReaderMenu ? addReaderMenu.dataset.contextPath : "";

function openAddReaderForm() {
    if (addReaderMenu) addReaderMenu.style.display = "flex";
}

function closeAddReaderForm() {
    if (addReaderMenu) {
        addReaderMenu.style.display = "none";
        resetReaderSearch();
    }
}


window.onclick = function (event) {
    if (event.target === addReaderMenu) {
        closeAddReaderForm();
    }
};



const readerSearchInput = document.getElementById("readerSearch");
const readerResultsBox = document.getElementById("readerResults");
const readerNameHidden = document.getElementById("readerName");
const addReaderSubmit = document.getElementById("addReaderSubmit");

let searchDebounce;

if (readerSearchInput) {
    readerSearchInput.addEventListener("input", () => {

        readerNameHidden.value = "";
        addReaderSubmit.disabled = true;

        const key = readerSearchInput.value.trim();
        clearTimeout(searchDebounce);

        if (key.length === 0) {
            renderReaderResults([]);
            return;
        }

        searchDebounce = setTimeout(() => searchReaders(key), 300);
    });
}

function searchReaders(key) {
    fetch(`${contextPath}/api/findreaders?key=${encodeURIComponent(key)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("No se pudo buscar usuarios");
            }
            return response.json();
        })
        .then(users => {
            renderReaderResults(users);
        })
        .catch(() => renderReaderResults([]));
}

function renderReaderResults(users) {
    readerResultsBox.innerHTML = "";

    if (users.length === 0) {
        readerResultsBox.classList.remove("visible");
        return;
    }

    users.forEach(user => {
        const option = document.createElement("div");
        option.className = "reader-result-item";
        option.textContent = user.userName;
        option.onclick = () => selectReader(user.userName);
        readerResultsBox.appendChild(option);
    });

    readerResultsBox.classList.add("visible");
}

function selectReader(userName) {
    readerSearchInput.value = userName;
    readerNameHidden.value = userName;
    addReaderSubmit.disabled = false;
    renderReaderResults([]);
}

function resetReaderSearch() {
    if (readerSearchInput) readerSearchInput.value = "";
    if (readerNameHidden) readerNameHidden.value = "";
    if (addReaderSubmit) addReaderSubmit.disabled = true;
    if (readerResultsBox) {
        readerResultsBox.innerHTML = "";
        readerResultsBox.classList.remove("visible");
    }
}