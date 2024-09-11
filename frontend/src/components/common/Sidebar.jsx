import {MdAlignHorizontalLeft, MdDashboard, MdOutlineSearch} from "react-icons/md";
import {Link, useLocation} from "react-router-dom";
import {BiLogOut} from "react-icons/bi";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import toast from "react-hot-toast";
import {VscRequestChanges} from "react-icons/vsc";

const Sidebar = () => {
    const queryClient = useQueryClient();
    const location = useLocation();

    const isActive = (path) => location.pathname === path;

    const {data: authUser} = useQuery({queryKey: ["authUser"]});
    const {mutate: logout} = useMutation({
        mutationFn: async () => {
            const res = await fetch("/api/logout", {
                method: "POST",
            });

            if (!res.ok) {
                const data = await res.json();
                throw new Error(data.error || "Something went wrong");
            }

        },
        onSuccess: () => {
            toast.success("Successfuly logged out")
            queryClient.invalidateQueries({queryKey: ["authUser"]});
        },
        onError: (error) => {
            toast.error(error.message);
        },
    });

    if (authUser.role === 'ROLE_ADMINS') {
        return (
            <div className='md:flex-[2_2_0] w-18 max-w-52'>
                <div className='sticky top-0 left-0 h-screen flex flex-col w-20 md:w-full'>
                    <ul className='flex flex-col gap-4 mt-4'>
                        <li className='flex justify-center md:justify-start'>
                            <Link
                                to='/'
                                className={`flex gap-3 items-center transition-all rounded-lg duration-300 py-4 px-4 w-full cursor-pointer 
                                ${isActive('/') ? 'bg-blue-500 text-white' : 'bg-white shadow-md hover:shadow-lg text-gray-700'}`}
                            >
                                <MdDashboard className={`w-8 h-8 ${isActive('/') ? 'text-white' : 'text-gray-700'}`}/>
                                <span
                                    className={`text-lg hidden md:block ${isActive('/') ? 'text-white' : 'text-gray-700'}`}>Dashboard</span>
                            </Link>
                        </li>
                        <li className='flex justify-center md:justify-start'>
                            <Link
                                to='/topics'
                                className={`flex gap-3 items-center transition-all rounded-lg duration-300 py-4 px-4 w-full cursor-pointer 
                                ${isActive('/topics') ? 'bg-blue-500 text-white' : 'bg-white shadow-md hover:shadow-lg text-gray-700'}`}
                            >
                                <MdAlignHorizontalLeft
                                    className={`w-6 h-6 ${isActive('/topics') ? 'text-white' : 'text-gray-700'}`}/>
                                <span
                                    className={`text-lg hidden md:block ${isActive('/topics') ? 'text-white' : 'text-gray-700'}`}>Topics</span>
                            </Link>
                        </li>
                    </ul>
                    {authUser && (
                        <div
                            className='mt-auto mb-10 flex gap-4 items-start bg-white shadow-md py-4 px-4 rounded-lg transition-all duration-300 hover:shadow-lg'
                        >
                            <div className='flex justify-between flex-1'>
                                <div className='hidden md:block'>
                                    <div className="flex flex-col text-gray-700 font-bold text-sm">
                                        <span>{authUser?.name}</span>
                                        <span>{authUser?.surname}</span>
                                    </div>
                                    <p className='text-slate-500 text-sm'>
                                        admin
                                    </p>
                                </div>
                                <BiLogOut
                                    className='w-5 h-5 cursor-pointer text-gray-700'
                                    onClick={(e) => {
                                        e.preventDefault();
                                        logout();
                                    }}
                                />
                            </div>
                        </div>
                    )}
                </div>
            </div>
        );
    }


    return (
        <div className='md:flex-[2_2_0] w-18 max-w-52'>
            <div className='sticky top-0 left-0 h-screen flex flex-col w-20 md:w-full'>
                <ul className='flex flex-col gap-4 mt-4'>
                    <li className='flex justify-center md:justify-start'>
                        <Link
                            to='/'
                            className={`flex gap-3 items-center transition-all rounded-lg duration-300 py-4 px-4 w-full cursor-pointer 
                                ${isActive('/') ? 'bg-blue-500 text-white' : 'bg-white shadow-md hover:shadow-lg text-gray-700'}`}
                        >
                            <MdDashboard className={`w-8 h-8 ${isActive('/') ? 'text-white' : 'text-gray-700'}`}/>
                            <span
                                className={`text-lg hidden md:block ${isActive('/') ? 'text-white' : 'text-gray-700'}`}>Dashboard</span>
                        </Link>
                    </li>
                    <li className='flex justify-center md:justify-start'>
                        <Link
                            to='/application'
                            className={`flex gap-3 items-center transition-all rounded-lg duration-300 py-4 px-4 w-full cursor-pointer 
                                ${isActive('/application') ? 'bg-blue-500 text-white' : 'bg-white shadow-md hover:shadow-lg text-gray-700'}`}
                        >
                            <VscRequestChanges
                                className={`w-6 h-6 ${isActive('/application') ? 'text-white' : 'text-gray-700'}`}/>
                            <span
                                className={`text-lg hidden md:block ${isActive('/application') ? 'text-white' : 'text-gray-700'}`}>Mentor Application</span>
                        </Link>
                    </li>
                    <li className='flex justify-center md:justify-start'>
                        <Link
                            to='/search'
                            className={`flex gap-3 items-center transition-all rounded-lg duration-300 py-4 px-4 w-full cursor-pointer 
                                ${isActive('/search') ? 'bg-blue-500 text-white' : 'bg-white shadow-md hover:shadow-lg text-gray-700'}`}
                        >
                            <MdOutlineSearch
                                className={`w-6 h-6 ${isActive('/search') ? 'text-white' : 'text-gray-700'}`}/>
                            <span
                                className={`text-lg hidden md:block ${isActive('/search') ? 'text-white' : 'text-gray-700'}`}>Mentor Search</span>
                        </Link>
                    </li>
                </ul>
                {authUser && (
                    <div
                        className='mt-auto mb-10 flex gap-4 items-start bg-white shadow-md py-4 px-4 rounded-lg transition-all duration-300 hover:shadow-lg'
                    >
                        <div className='flex justify-between flex-1'>
                            <div className='hidden md:block'>
                                <div className="flex flex-col text-gray-700 font-bold text-sm">
                                    <span>{authUser?.name}</span>
                                    <span>{authUser?.surname}</span>
                                </div>
                                <p className='text-slate-500 text-sm'>
                                    user
                                </p>
                            </div>
                            <BiLogOut
                                className='w-5 h-5 cursor-pointer text-gray-700'
                                onClick={(e) => {
                                    e.preventDefault();
                                    logout();
                                }}
                            />
                        </div>
                    </div>
                )}
            </div>
        </div>
    );


};
export default Sidebar;
