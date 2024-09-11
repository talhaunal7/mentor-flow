import {useQuery, useQueryClient} from "@tanstack/react-query";
import {Link} from "react-router-dom";

const Mentorship = ({mentorship, type}) => {
    const {data: authUser} = useQuery({queryKey: ["authUser"]});
    const queryClient = useQueryClient();


    return (
        <tr className="hover:bg-gray-50 transition-colors">
            <td className="border-t border-gray-200 px-4 py-2">
                {type === "mentor" ? mentorship.menteeName : mentorship.mentorName}
            </td>
            <td className="border-t border-gray-200 px-4 py-2">
                {mentorship.topic}
            </td>
            <td className="border-t border-gray-200 px-4 py-2">
            <span
                className={`px-3 py-1 rounded-full text-sm font-medium ${
                    mentorship.status === 'NOT_STARTED'
                        ? 'bg-yellow-100 text-yellow-800'
                        : mentorship.status === 'IN_PROGRESS'
                            ? 'bg-blue-100 text-blue-800'
                            : mentorship.status === 'COMPLETED'
                                ? 'bg-green-100 text-green-800'
                                : ''
                }`}
            >
                {mentorship.status === 'NOT_STARTED' && 'Not Started'}
                {mentorship.status === 'IN_PROGRESS' && 'In Progress'}
                {mentorship.status === 'COMPLETED' && 'Completed'}
            </span>
            </td>
            <td className="border-t border-gray-200 px-4 py-2 text-right">
                <Link
                    to={`/mentorship/${mentorship.id}`}
                    key={mentorship.id}
                    className="text-blue-500 hover:underline text-m"
                >
                    Mentorship Details
                </Link>
            </td>
        </tr>
    );


};
export default Mentorship;
