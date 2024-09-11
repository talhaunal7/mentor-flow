import {Link} from "react-router-dom";

const Mentor = ({mentor}) => {


    return (
        <tr className="hover:bg-gray-100">
            <td className="px-6 py-4 whitespace-nowrap">
                {mentor.name} {mentor.surname}
            </td>
            <td className="px-6 py-4 whitespace-nowrap">
                {mentor.topic}
            </td>
            <td className="px-6 py-4 whitespace-nowrap">
                {mentor.subtopics.join(", ")}
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
export default Mentor;