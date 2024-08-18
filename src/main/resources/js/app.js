document.addEventListener('DOMContentLoaded', async function () {
    await showUsernameOnNavbar()
    await fillTableOfAllUsers();
    await fillTableAboutCurrentUser();
    await addNewUserForm();
    await DeleteModalHandler();
    await EditModalHandler();
});

const ROLE_USER = {id: 1, name: "ROLE_USER"};
const ROLE_ADMIN = {id: 2, name: "ROLE_ADMIN"};

async function showUsernameOnNavbar() {
    const currentUsernameNavbar = document.getElementById("currentUsernameNavbar")
    const currentUser = await dataAboutCurrentUser();
    currentUsernameNavbar.innerHTML =
        `<strong>${currentUser.username}</strong>
                 with roles: 
                 ${currentUser.roles.map(role => role.roleNameWithoutRole).join(' ')}`;
}
