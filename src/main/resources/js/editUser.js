async function sendDataEditUser(user) {
    await fetch("/api/admin" ,
        {method:"PUT", headers: {'Content-type': 'application/json'}, body: JSON.stringify(user)} )
}

const modalEdit = document.getElementById("editModal");

async function EditModalHandler() {
    await fillModal(modalEdit);
}

modalEdit.addEventListener("submit", async function(event){
    event.preventDefault();

    const rolesSelected = document.getElementById("rolesEdit");

    const currentRoles = await getUserDataById(document.getElementById("idEdit").value)

    let roles = [];
    if (rolesSelected.selectedOptions.length > 0) {
        for (let option of rolesSelected.selectedOptions) {
            if(option.value === ROLE_USER.name) {
                roles.push(ROLE_USER);
            } else if (option.value === ROLE_ADMIN.name) {
                roles.push(ROLE_ADMIN);
            }
        }
    } else {
        for (let option of currentRoles.roles) {
            if (option.name === ROLE_USER.name) {
                roles.push(ROLE_USER);
            } else if (option.name === ROLE_ADMIN.name) {
                roles.push(ROLE_ADMIN);
            }
        }
    }


    let user = {
        id: document.getElementById("idEdit").value,
        name: document.getElementById("firstNameEdit").value,
        lastname: document.getElementById("lastNameEdit").value,
        age: document.getElementById("ageEdit").value,
        username: document.getElementById("usernameEdit").value,
        password: document.getElementById("passwordEdit").value,
        roles: roles
    }

    await sendDataEditUser(user).catch();
    await fillTableOfAllUsers();

    const modalBootstrap = bootstrap.Modal.getInstance(modalEdit);
    modalBootstrap.hide();
})
