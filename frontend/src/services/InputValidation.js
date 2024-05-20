const validateLogin = ({ username, password }) => {
    username=String(username);
    password=String(password);

    var emailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

    if (username.length===0 && password.length===0) {
        alert('Please Enter Email and password');
        return false;
    }
    if (username.length<1) {
        alert(" Please Enter Your Email ");
        return false;
    }
    else if (!username.match(emailformat))
    {
        alert("Please Enter Valid Email");
        return false;
    }

    if (password.length===0) {
        alert('Please Enter Password');
        return false;
    }
    return true;
};


const LoginInputValidation = ({ username, password }) => validateLogin({ username, password });

export { LoginInputValidation};
