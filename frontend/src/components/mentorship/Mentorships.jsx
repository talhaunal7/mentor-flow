import Mentorship from "./Mentorship.jsx";

import {useQuery} from "@tanstack/react-query";
import {useEffect} from "react";

const Mentorships = ({type, username, userId}) => {

    const getCourseEndpoint = () => {
        switch (type) {
            case "mentor":
                return "/api/mentorships/mentor-processes";
            case "mentee":
                return "/api/mentorships/mentee-processes";
            default:
                return "/api/posts/all";
        }
    };

    const COURSE_ENDPOINT = getCourseEndpoint();

    const {
        data: mentorships,
        isLoading,
        refetch,
        isRefetching
    } = useQuery({
        queryKey: ["mentorships"],
        queryFn: async () => {
            try {
                const res = await fetch(COURSE_ENDPOINT);
                const data = await res.json();

                if (!res.ok) {
                    throw new Error(data.error || "Something went wrong");
                }

                return data;
            } catch (error) {
                throw new Error(error);
            }
        },
    });

    useEffect(() => {
        refetch();
    }, [type, refetch, username]);



    return (
        <>
            {(isLoading || isRefetching) && (
                <div className="flex flex-col justify-center items-center py-10">
                    <p className="text-gray-500">Loading...</p>
                </div>
            )}
            {!isLoading && !isRefetching && mentorships && (
                <div className="p-4">
                    <div className="overflow-x-auto bg-white shadow-md rounded-lg">
                        <table className="w-full table-auto">
                            <thead className="bg-gray-100">
                            <tr>
                                <th className="text-left px-4 py-2">{type === "mentor" ? "Mentee" : "Mentor"}</th>
                                <th className="text-left px-4 py-2">Subject</th>
                                <th className="text-left px-4 py-2">Course Status</th>
                                <th className="text-left px-4 py-2"></th>
                            </tr>
                            </thead>
                            <tbody>
                            {mentorships.map((mentorship) => (
                                <Mentorship key={mentorship.id} mentorship={mentorship} type={type} />
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}
        </>
    );

};
export default Mentorships;
