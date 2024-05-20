import React, { useState, useEffect } from "react";
import { IoNotificationsOutline } from "react-icons/io5";
import { RxDividerVertical } from "react-icons/rx";
import { useLocation, useNavigate } from "react-router-dom";
import webServerUrl from "../configurations/WebServer";
import HttpService from "../services/HttpService";

import logoImage from "../assets/favicon.png";
import profileImage from "../assets/boy.png";

const DropdownMenu = ({ Logout }) => (
    <div className="absolute right-0 mt-2 w-40 bg-white border rounded shadow-lg">
      <ul>
        <li className="cursor-pointer px-4 py-2 hover:bg-gray-100" onClick={Logout}>Logout</li>
      </ul>
    </div>
  );

const TopNavigationMenu = ({open, setOpen}) => {

    const [role, setRole] = useState("");
    const [showLogout, setShowLogout] = useState("");
    const [username, setUsername] = useState("");

    const location = useLocation();
    const navigate = useNavigate();


    useEffect(() => {
        const data = JSON.parse(window.localStorage.getItem('Data'));
        setRole(data.role);
        setUsername(data.username);
    },[]); 

    const navigateOnboarding = () => {
        navigate("doctorOnboarding");
    };

    const navigateStatus = () => {
        navigate("doctorStatus");
    };

    const toggleLogout = () => {
        setShowLogout(!showLogout);
    }

    const handleLogout = async() => {
        // const logoutURL=webServerUrl+"/suhrud/logout";  
        const sessionData = await localStorage.getItem('Data');
        const localData=JSON.parse(sessionData);
        const method='POST';
        const bearerToken = localData.token;

        let logoutURL;
        
        if(localData.role === "doctor"){
            logoutURL=webServerUrl+"/suhrud/doctor/logout"; 
        }
        if(localData.role === "admin"){
            logoutURL=webServerUrl+"/suhrud/logout"; 
        }
        
        const headers = {
            'Authorization': `Bearer ${bearerToken}`, // Include your token here
            'Content-Type': 'String', // Specify the content type if needed
        };
        try{
            const response=await HttpService(method,logoutURL,null,headers);
            console.log(response.status)
            if(response.status===200){
              console.log("Successful");
              console.log(response.data);
              //alert(response.data);
              localStorage.removeItem('Data');
              navigate('/login');
            }else{
                alert(response.data);
            }  
        }catch(error){
            alert(error.data);
        }
    };

    return (
        <>

            <div className={`bg-white relative w-full h-14 rounded-2xl mb-5`} >
                <div className="flex justify-between items-center p-2">
                    <div>
                        {!open && <img src={logoImage} className = "w-10 h-10 rounded-full ml-8 cursor-pointer"/> }
                    </div>

                    {role === "admin" ? (
                        <div className="flex flex-row items-center w-full justify-between">

                            {/* two buttons */}
                            <div className="rounded-3xl ml-12 self-start">
                                
                                <button 
                                    className={`hover:bg-cyan-100 rounded-3xl px-4 py-2 ${location.pathname === "/doctorOnboarding" ? "bg-cyan-200 text-black font-semibold" : ""}`}
                                    onClick={() => navigateOnboarding()}
                                >
                                    Doctor Onboarding
                                </button>
                                <button 
                                    className={`hover:bg-cyan-100 rounded-3xl px-4 py-2 ml-4 ${location.pathname === "/doctorStatus" ? "bg-cyan-200 text-black font-semibold" : ""}`}
                                    onClick={() => navigateStatus()}
                                >
                                    Doctor Status
                                </button>
                            
                            </div>
                            
                            {/* profile ame and profile image */}
                            <div className="flex self-end items-center justify-between">
                                <p className="font-semibold">Admin</p>
                                <div className="relative">
                                    <img 
                                        src={profileImage} 
                                        className = "w-10 h-10 rounded-full mx-4 cursor-pointer" 
                                        onClick={() => toggleLogout()}
                                    />
                                    
                                    {showLogout &&    
                                        (
                                            <DropdownMenu
                                                Logout = {() => handleLogout()}
                                            />
                                        )
                                    }
                                </div>

                            </div>
                        </div>
                    )
                    
                    :

                    (
                        <>
                        {/* <div className="flex-1 rounded-3xl ml-8">
                        <input placeholder="Search" className="bg-gray-100 self-start pl-10 p-2 rounded-3xl w-2/4 hover:bg-gray-200 hover:text-black"/>

                        </div> */}

                        <div className="flex self-end items-center justify-between">
                            {/* <IoNotificationsOutline 
                                size={25} 
                                className="cursor-pointer"/>

                            <RxDividerVertical size={40}/> */}
                            <p>{username}</p>
                            <div className="relative">
                                <img 
                                    src={profileImage} 
                                    className = "w-10 h-10 rounded-full mx-4 cursor-pointer" 
                                    onClick={() => toggleLogout()}    
                                />

                                {showLogout &&    
                                        (
                                            <DropdownMenu
                                                Logout = {() => handleLogout()}
                                            />
                                        )
                                }
                            </div>

                        </div>
                        </>
                    )
                
                    }

                    
                </div>
            </div>
        
        </>
    );
}

export default TopNavigationMenu;