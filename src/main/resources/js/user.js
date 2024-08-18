document.addEventListener('DOMContentLoaded', async function () {
    await showUsernameOnNavbar()
    await fillTableAboutUser();
});

async function dataAboutCurrentUser() {
    const response = await fetch("/api/user")
    return await response.json();
}
async function fillTableAboutUser(){
    const currentUserTable1 = document.getElementById("currentUserTable");
    const currentUser = await dataAboutCurrentUser();

    let currentUserTableHTML = "";
    currentUserTableHTML +=
        `<tr>
            <td>${currentUser.id}</td>
            <td>${currentUser.name}</td>
            <td>${currentUser.lastname}</td>
            <td>${currentUser.age}</td>
            <td>${currentUser.username}</td>
            <td>${currentUser.roles.map(role => role.roleNameWithoutRole).join(' ')}</td>
        </tr>`
    currentUserTable1.innerHTML = currentUserTableHTML;
}

async function showUsernameOnNavbar() {
    const currentUsernameNavbar = document.getElementById("currentUsernameNavbar")
    const currentUser = await dataAboutCurrentUser();
    currentUsernameNavbar.innerHTML =
        `<strong>${currentUser.username}</strong>
                 with roles: 
                 ${currentUser.roles.map(role => role.roleNameWithoutRole).join(' ')}`;
}

