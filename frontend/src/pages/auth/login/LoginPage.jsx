import {useState} from "react";
import {MdOutlineMail, MdPassword} from "react-icons/md";
import {SiCodementor} from "react-icons/si";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import GoogleSignIn from "./GoogleLogin.jsx";
import {toast} from "react-hot-toast";

const LoginPage = () => {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
    });
    const queryClient = useQueryClient();

    const onGoogleSignIn = useMutation({
        mutationFn: async (res) => {
            const {credential} = res;
            try {
                const loginResponse = await fetch("/api/oauth/login", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({idToken: credential}),
                });

                if (!loginResponse.ok) {
                    const data = await loginResponse.json();
                    throw new Error(data.error || "Something went wrong");
                }

            } catch (error) {
                throw new Error(error.message || "Bad credentials");
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ["authUser"]});
        },
        onError: (error) => {
            toast.error("Google login failed !")
        },
    });

    const handleGoogleSignIn = (res) => {
        onGoogleSignIn.mutate(res);
    };

    const {
        mutate: loginMutation,
        isPending,
    } = useMutation({
        mutationFn: async ({username, password}) => {
            const res = await fetch("/api/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({username, password}),
            });
            if (!res.ok) {
                throw new Error("Something went wrong");
            }
        },
        onSuccess: () => {
            toast.success("Login successful")
            queryClient.invalidateQueries({queryKey: ["authUser"]});
        }, onError: () => {
            toast.error("Bad credentials !")
        }
    });

    const handleSubmit = (e) => {
        e.preventDefault();
        loginMutation(formData);
    };

    const handleInputChange = (e) => {
        setFormData({...formData, [e.target.name]: e.target.value});
    };

    return (
        <div className="max-w-screen-xl mx-auto flex flex-col lg:flex-row h-screen">

            <div
                className="flex-1 flex flex-col justify-center items-center px-6 lg:px-12 py-8 bg-white shadow-md rounded-lg">
                <SiCodementor className="text-4xl"/>
                <form className="w-full max-w-md space-y-6" onSubmit={handleSubmit}>
                    <h1 className="text-4xl font-bold text-gray-800 mb-6 text-center">Login</h1>

                    <div className="flex flex-col space-y-4">
                        <label className="input input-bordered rounded flex items-center gap-2 p-3 bg-gray-100">
                            <MdOutlineMail className="text-gray-500"/>
                            <input
                                type="text"
                                className="grow bg-transparent focus:outline-none"
                                placeholder="Username"
                                name="username"
                                onChange={handleInputChange}
                                value={formData.username}
                            />
                        </label>

                        <label className="input input-bordered rounded flex items-center gap-2 p-3 bg-gray-100">
                            <MdPassword className="text-gray-500"/>
                            <input
                                type="password"
                                className="grow bg-transparent focus:outline-none"
                                placeholder="Password"
                                name="password"
                                onChange={handleInputChange}
                                value={formData.password}
                            />
                        </label>
                    </div>

                    <button
                        className="btn rounded-full w-full py-3 mt-6 bg-indigo-600 text-white hover:bg-indigo-700 transition duration-300">
                        {isPending ? "Loading..." : "Login"}
                    </button>
                </form>

                <div className="mt-6">
                    <GoogleSignIn onGoogleSignIn={handleGoogleSignIn}/>
                </div>

            </div>
        </div>
    );


};
export default LoginPage;
