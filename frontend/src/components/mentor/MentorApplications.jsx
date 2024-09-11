import {useQuery} from "@tanstack/react-query";
import MentorApplication from "./MentorApplication.jsx";

const MentorApplications = ({type}) => {
    const MENTORS_ENDPOINT = "/api/mentors";

    const {
        data: mentors,
        isLoading,
        isRefetching,
    } = useQuery({
        queryKey: ["mentors"],
        queryFn: async () => {
            try {
                const res = await fetch(MENTORS_ENDPOINT);
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

    return (
        <>
            {(isLoading || isRefetching) && (
                <div className="flex justify-center items-center h-64">
                    <div className="spinner-border animate-spin inline-block w-8 h-8 border-4 rounded-full"
                         role="status">
                        <span className="visually-hidden"></span>
                    </div>
                </div>
            )}
            {!isLoading && !isRefetching && mentors?.length === 0 && (
                <p className="text-center my-4 text-lg text-gray-500">
                    No mentors found.
                </p>
            )}
            {!isLoading && !isRefetching && mentors && (
                <div className="overflow-x-auto shadow-md sm:rounded-lg">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Mentor
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Topic
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Status
                            </th>
                            <th className="relative px-6 py-3">
                                <span className="sr-only">Details</span>
                            </th>
                        </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                        {mentors.map((mentor) => (
                            <MentorApplication key={mentor.id} mentor={mentor}/>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </>
    );
};

export default MentorApplications;
