import {useState, useEffect} from "react";
import Mentorships from "../../components/mentorship/Mentorships.jsx";
import {useQuery} from "@tanstack/react-query";
import Mentors from "../../components/mentor/Mentors.jsx";
import MentorApplications from "../../components/mentor/MentorApplications.jsx";

const DashBoard = () => {
    const {data: authUser} = useQuery({queryKey: ["authUser"]});
    const [courseType, setCourseType] = useState("mentor");

    useEffect(() => {
        if (authUser?.role === "ROLE_ADMINS") {
            setCourseType("admin");
        } else if (authUser?.role === "ROLE_USERS") {
            setCourseType("mentor");
        }
    }, [authUser]);

    return (
        <>
            <div className='flex-[4_4_0] mr-auto min-h-screen'>
                {authUser?.role === "ROLE_ADMINS" && (
                    <div className='flex w-full'>
                        <div className="flex justify-center flex-1 p-3 text-xl font-bold">
                            Mentor Applications
                        </div>
                    </div>

                )}

                {authUser?.role === "ROLE_USERS" && (
                    <div className='flex w-full'>
                        <div
                            className={
                                "flex justify-center rounded-full flex-1 p-3 text-xl font-bold hover:bg-gray-300 transition duration-300 cursor-pointer relative"
                            }
                            onClick={() => setCourseType("mentor")}
                        >
                            Mentorship
                            {courseType === "mentor" && (
                                <div className='absolute bottom-0 w-10 h-1 rounded-full bg-primary'></div>
                            )}
                        </div>
                        <div
                            className='flex justify-center rounded-full flex-1 p-3 text-xl font-bold hover:bg-gray-300 transition duration-300 cursor-pointer relative'
                            onClick={() => setCourseType("mentee")}
                        >
                            Mentee
                            {courseType === "mentee" && (
                                <div className='absolute bottom-0 w-10 h-1 rounded-full bg-primary'></div>
                            )}
                        </div>
                    </div>
                )}

                <div>
                    {authUser.role === "ROLE_USERS" ? (
                        <Mentorships type={courseType}/>
                    ) : authUser.role === "ROLE_ADMINS" ? (
                        <MentorApplications/>
                    ) : (
                        <div>Unauthorized</div>
                    )}
                </div>


            </div>
        </>
    );


};

export default DashBoard;
