import React, { useEffect, useState } from "react";
import logoImage from "../assets/favicon.png";
import { IoIosMenu, IoIosHome, IoIosLogOut } from "react-icons/io";
import { FaRegUser } from "react-icons/fa";
import { AiOutlineSchedule, AiOutlineQuestionCircle } from "react-icons/ai";
import { PiChats } from "react-icons/pi";
import { CgProfile } from "react-icons/cg";
import { useLocation, useNavigate } from "react-router-dom";
import webServerUrl from "../configurations/WebServer";
import HttpService from "../services/HttpService";

const SideNavigationMenu = ({ open, setOpen }) => {
    const location = useLocation();
    const navigate = useNavigate();

    const [role, setRole] = useState("");

    useEffect(() => {
        const data = JSON.parse(window.localStorage.getItem('Data'));
        setRole(data.role);
    },[]); 


    const Menus = [
        { title: "Dashboard", icon: <IoIosHome size={25} />, src: "/main" },
        { title: "Patients", icon: <FaRegUser size={25} />, src: "/patients" },
        { title: "Appointments", icon: <AiOutlineSchedule size={25} />, src: "/appointments" },
        { title: "Chats", icon: <PiChats size={25} />, src: "/chatpage" },
        { title: "QnaForum", icon: <AiOutlineQuestionCircle size={25} />, src: "/qnaForum" },
    ];

    const AccountMenus = [
        { title: "Profile", icon: <CgProfile size={25} />, src: "/profile" },
        { title: "Logout", icon: <IoIosLogOut size={25} />, src: "/login" },
    ];

    const collapse = () => {
        setOpen(!open);
    };

    const navigateto = (src) => {
        navigate(src);
    };

    const handleLogout = async() => {
        const logoutURL=webServerUrl+"/suhrud/doctor/logout";  
        const sessionData = await localStorage.getItem('Data');
        const localData=JSON.parse(sessionData);
        const method='POST';
        const bearerToken = localData.token;
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
            <div className={`bg-white relative ${open ? "w-96" : "w-16"} h-full rounded-3xl duration-500`} >
                <div className={`flex border-b border-neutral-600 h-14 justify-between items-center`}>
                    {open && <img src={logoImage} className="w-10 h-10 rounded-full ml-8" />}
                    <IoIosMenu
                        size={45}
                        className="mr-5 ml-5 cursor-pointer"
                        onClick={() => collapse()}
                    />
                </div>

                <div className="mt-10">
                    {open ? (
                        <>
                            {Menus.map((menu, index) => (
                                <div
                                    key={index}
                                    className={`text-base p-2 rounded-full mx-5 my-3 text-center cursor-pointer hover:bg-cyan-200 hover:text-black
                            ${location.pathname === menu.src ? "bg-cyan-200 text-black font-semibold" : "text-slate-500"}`}
                                    onClick={() => navigateto(menu.src)}
                                >
                                    {menu.title}
                                </div>
                            ))}

                            <p className="ml-10 mt-10 font-semibold">Account</p>

                            {AccountMenus.map((menu, index) => (
                                <div
                                    key={index}
                                    className={`text-base p-2 rounded-full mx-5 my-3 text-center cursor-pointer hover:bg-cyan-200 hover:text-black
                                ${location.pathname === menu.src ? "bg-cyan-200 text-black" : "text-slate-500"}`}
                                    onClick={menu.title === "Logout" ? handleLogout : () => navigateto(menu.src)}
                                >
                                    {menu.title}
                                </div>
                            ))}
                        </>
                    ) : (
                        <>
                            {Menus.map((menu, index) => (
                                <div
                                    key={index}
                                    className={`p-2 rounded-full text-center cursor-pointer mx-3 my-2
                            ${location.pathname === menu.src ? "bg-cyan-200 text-black font-semibold" : "text-slate-500"}`}
                                    onClick={() => navigateto(menu.src)}
                                >
                                    {menu.icon}
                                </div>
                            ))}

                            <div className="m-20"></div>

                            {AccountMenus.map((menu, index) => (
                                <div
                                    key={index}
                                    className={`p-2 rounded-full mx-3 my-2 text-center cursor-pointer
                                ${location.pathname === menu.src ? "bg-cyan-200 text-black" : "text-slate-500"}`}
                                    onClick={menu.title === "Logout" ? handleLogout : () => navigateto(menu.src)}
                                >
                                    {menu.icon}
                                </div>
                            ))}
                        </>
                    )}
                </div>
            </div>
        </>
    );
};

export default SideNavigationMenu;
