import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import loginbackground from "../assets/loginbackground.jpg";
import { LoginInputValidation } from "../services/InputValidation";
import HttpService from "../services/HttpService.js";
import { MdDarkMode, MdOutlineLightMode } from "react-icons/md";
import webServerUrl from "../configurations/WebServer.js";

const Login = ({ setAuthenticated }) => {
    const [selectedOption, setSelectedOption] = useState('Applicant');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('Applicant');
    const [isSignUp, setIsSignUp] = useState(false);

    const navigate = useNavigate();

    const [dark, setDark] = useState(false);

    const darkModeHandler = () => {
        setDark(!dark);
        document.body.classList.toggle("dark");
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        console.log(selectedOption);
        const isValid = LoginInputValidation({ username, password });
        if (!isValid) return;

        if (selectedOption === 'Applicant') {
            setRole('Applicant');
        } else {
            setRole('HR');
        }

        const method = 'POST';
        const data = {
            email: username,
            password: password,
            role: selectedOption
        };
        console.log("data:");
        console.log(data);

        try {
            const loginURL = isSignUp ? webServerUrl+"auth/register" : webServerUrl+"auth/authenticate";
            console.log("login url"+loginURL);
            const response = await HttpService(method, loginURL, data);
            console.log(response.status);

            if (response.status === 200) {
                console.log("Successful");
                console.log(response.data);
                try {
                    const userData = {
                        ...response.data,
                        role: selectedOption
                    };
                    if(!isSignUp){
                        await window.localStorage.setItem('Data', JSON.stringify(userData));
                        setAuthenticated(true);
                    }
                   
                } catch (error) {
                    console.log("error while saving data");
                    console.log(error);
                }
                if (selectedOption === 'Applicant') {
                    console.log("hello applicant");
                    if(isSignUp){
                        alert("Successfully Registered. Please login with your credentials");
                        window.location.reload();
                    }
                        
                    else
                        navigate('/main');
                } else {
                    console.log("HR");
                    if(isSignUp){
                        alert("Successfully Registered. Please login with your credentials");
                        window.location.reload();
                    }
                    else
                        navigate('/main');
                }
            } else {
                console.log("else part error:");
                alert(response.data.message);
                setPassword("");
                setUsername("");
            }
        } catch (error) {
            console.log("catch block of error");
            alert(error.data.message);
            setPassword("");
            setUsername("");
            window.location.reload();
        }

    };

    const handleOptionChange = (option) => {
        setSelectedOption(option);
    };

    return (
        <div className="flex flex-col h-full dark:bg-blue-950">
            <div className="flex flex-grow justify-center items-center">
                <div className="h-full w-1/2 flex flex-col justify-center items-center content-center">
                    <h2 className="text-4xl font-bold text-gray-800 mb-4 dark:text-white">Welcome to Job Portal!</h2>
                    <p className="text-gray-600 mb-4 dark:text-gray-200">NAMASKAR</p>
                    <button onClick={darkModeHandler}>
                        {
                            dark && <MdOutlineLightMode /> // render sunny when dark is true
                        }
                        {
                            !dark && <MdDarkMode /> // render moon when dark is false
                        }
                    </button>
                </div>

                <div className="w-1/2 h-full flex justify-center items-center">
                    <div className="relative h-full w-3/5 rounded-3xl">
                        <img src={loginbackground} alt="Background" className="w-full h-full object-cover rounded-3xl" />
                        <div className="absolute w-4/5 top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white p-8 rounded-3xl shadow-lg flex flex-col items-center">
                            <h6 className="text-2xl font-semibold mb-5">{isSignUp ? "SIGN UP" : "LOGIN"}</h6>
                            <form className="w-full" onSubmit={handleSubmit}>
                                <div className="mb-5 w-full rounded-3xl overflow-hidden">
                                    <input type="text" placeholder="Email" id="username" name="username" className="w-full p-2 py-3 pl-10 rounded-3xl bg-cyan-200"
                                        onChange={(text) => { setUsername(text.target.value) }}
                                    />
                                </div>
                                <div className="mb-1 rounded-3xl overflow-hidden">
                                    <input type="password" placeholder="Password" id="password" name="password" className="w-full p-2 py-3 pl-10 rounded-3xl bg-cyan-200"
                                        onChange={(text) => { setPassword(text.target.value) }}
                                    />
                                </div>
                                <div className="mb-4 flex justify-between">
                                    <div className="flex justify-center items-center ml-2">
                                        <input
                                            type="checkbox"
                                            id="HR"
                                            name="HR"
                                            value="HR"
                                            checked={selectedOption === 'HR'}
                                            onChange={() => handleOptionChange('HR')}
                                            className="cursor-pointer mr-1"
                                        />
                                        <label htmlFor="HR" className="cursor-pointer text-xs text-gray-500 hover:text-black hover:underline">HR</label>
                                    </div>
                                    <div>
                                        <button
                                            type="button"
                                            onClick={() => setIsSignUp(!isSignUp)}
                                            className="text-xs text-gray-800 font-bold hover:text-black hover:underline"
                                        >
                                            {isSignUp ? "Login" : "Sign Up"}
                                        </button>
                                    </div>
                                </div>
                                <button type="submit" className="bg-blue-500 dark:bg-blue-900 text-white px-4 py-3 w-full rounded-3xl hover:bg-blue-600">Submit</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
