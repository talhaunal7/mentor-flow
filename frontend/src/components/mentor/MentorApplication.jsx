import {Link} from "react-router-dom";

const MentorApplication = ({mentor}) => {

    return (
        <tr className="hover:bg-gray-100">
            <td className="px-6 py-4 whitespace-nowrap">
                {mentor.name} {mentor.surname}
            </td>
            <td className="px-6 py-4 whitespace-nowrap">
                {mentor.topic}
            </td>
            <td className="px-6 py-4 whitespace-nowrap">
                <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                    mentor.status === 'APPROVED'
                        ? 'bg-green-100 text-green-800'
                        : 'bg-red-100 text-red-800'
                }`}>
                    {mentor.status}
                </span>
            </td>

            <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <Link
                    to={`/mentors/${mentor.id}`}
                    key={mentor.id}
                    className="text-indigo-600 hover:text-indigo-900"
                >
                    Mentor Details
                </Link>
            </td>
        </tr>
    );
};
export default MentorApplication;