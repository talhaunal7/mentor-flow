import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {Link, useNavigate, useParams} from "react-router-dom";
import {useState} from "react";
import toast from "react-hot-toast";
import {FaStar} from "react-icons/fa";

const MentorshipDetailPage = () => {
    const {id} = useParams();
    const queryClient = useQueryClient();
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [currentPhaseId, setCurrentPhaseId] = useState(null);
    const navigate = useNavigate()

    const {
        data: mentorship,
        isLoading: isMentorshipLoading,
        isRefetching: isMentorshipRefetching,
    } = useQuery({
        queryKey: ["mentorship"],
        queryFn: async () => {
            try {
                const res = await fetch(`/api/mentorships/${id}`);
                const data = await res.json();
                if (!res.ok) {
                    throw new Error(data.error || "Something went wrong");
                }
                return data;
            } catch (e) {
                toast.error(e.message)
                navigate("/")
            }

        }
    });

    const {
        data: phases,
    } = useQuery({
        queryKey: ["phases"],
        queryFn: async () => {
            const res = await fetch(`/api/phases/${id}`);
            const data = await res.json();
            if (!res.ok) {
                throw new Error(data.error || "Something went wrong");
            }
            return data;
        }
    });

    const completePhaseMutation = useMutation({
        mutationFn: async ({phaseId, review, rating}) => {
            const res = await fetch(`/api/phases/${phaseId}/completion`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({review, rating}),
            });
            if (!res.ok) {
                const data = await res.json()
                throw new Error(data.error);
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries(["phases"])
            toast.success("Phase completed successfully!");
            setIsModalOpen(false);
        },
        onError: (error) => {
            toast.error(error.message);
        },
    });

    const handleCompleteOrReviewClick = (phaseId) => {
        setCurrentPhaseId(phaseId);
        setIsModalOpen(true);
    };

    const handleModalSubmit = (review, rating) => {
        completePhaseMutation.mutate({phaseId: currentPhaseId, review, rating});
    };

    if (isMentorshipLoading || isMentorshipRefetching) {
        return (
            <div className="flex justify-center items-center h-screen">
                <div className="text-lg text-center">Loading...</div>
            </div>
        );
    }


    const renderStars = (rating) => {
        const stars = [];
        for (let i = 0; i < 5; i++) {
            stars.push(
                <FaStar
                    key={i}
                    className={i < rating ? "text-yellow-500" : "text-gray-400"}
                />
            );
        }
        return stars;
    };

    return (
        <div className="flex-[4_4_0] border-l border-r border-gray-700 min-h-screen bg-base-100">
            <div className="flex justify-between items-center p-6 border-b border-gray-700 shadow-lg">
                <h1 className="font-bold text-2xl">Mentorship Details</h1>
            </div>

            <div className="p-6">
                <div className="border-b border-gray-700 p-6 rounded-lg shadow-sm bg-base-100">
                    <p className="text-xl mb-2">
                        <span className="font-bold">Mentor:</span> {mentorship.mentorName}
                    </p>
                    <p className="text-xl mb-2">
                        <span className="font-bold">Mentee:</span> {mentorship.menteeName}
                    </p>
                    <p className="text-xl mb-2">
                        <span className="font-bold">Start Date:</span>{" "}
                        {new Date(mentorship.startDate).toLocaleDateString()} at{" "}
                        {new Date(mentorship.startDate).toLocaleTimeString()}
                    </p>
                    <p className="text-xl mb-1">
                        <strong>Status:</strong>
                        <span
                            className={`px-3 py-1 rounded-full text-xl font-medium ${
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
                    </p>

                    {mentorship.status === "NOT_STARTED" && (
                        <Link
                            to={`/plan/${mentorship.id}`}
                            key={mentorship.id}
                            className="mt-4 inline-block px-4 py-2 border border-transparent text-md font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700"
                        >
                            Plan Mentorship
                        </Link>
                    )}
                </div>

                <ul className="mt-6 space-y-6">
                    {phases?.map((phase) => (
                        <li key={phase.id} className="border-b border-gray-700 p-6 rounded-lg shadow-sm bg-base-100">
                            <p className="text-md">
                                <span className="font-bold mb-1">Phase Name:</span> {phase.name}
                            </p>
                            <p className="text-md mb-1">
                                <strong>Status:</strong>
                                <span
                                    className={`px-3 py-1 rounded-full text-md font-medium ${
                                        phase.status === 'NOT_STARTED'
                                            ? 'bg-yellow-100 text-yellow-800'
                                            : phase.status === 'ACTIVE'
                                                ? 'bg-blue-100 text-blue-800'
                                                : phase.status === 'COMPLETED'
                                                    ? 'bg-green-100 text-green-800'
                                                    : ''
                                    }`}
                                >
                                     {phase.status === 'NOT_STARTED' && 'Not Started'}
                                    {phase.status === 'ACTIVE' && 'Active'}
                                    {phase.status === 'COMPLETED' && 'Completed'}
                             </span>
                            </p>
                            <p className="text-md mb-1">
                                <span className="font-bold">End Date:</span>{" "}
                                {new Date(phase.endDate).toLocaleDateString()} at{" "}
                                {new Date(phase.endDate).toLocaleTimeString()}
                            </p>


                            {phase.status === "ACTIVE" && !phase.mentorRating && !phase.menteeRating && (
                                <button
                                    className="mt-4 bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600"
                                    onClick={() => handleCompleteOrReviewClick(phase.id)}
                                >
                                    Complete
                                </button>
                            )}

                            {phase.status === "COMPLETED" && mentorship.mentorFlag && !phase.mentorRating && (
                                <button
                                    className="mt-4 bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600"
                                    onClick={() => handleCompleteOrReviewClick(phase.id)}
                                >
                                    Review as Mentor
                                </button>
                            )}

                            {phase.status === "COMPLETED" && !mentorship.mentorFlag && !phase.menteeRating && (
                                <button
                                    className="mt-4 bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600"
                                    onClick={() => handleCompleteOrReviewClick(phase.id)}
                                >
                                    Review as Mentee
                                </button>
                            )}

                            {phase.mentorRating !== null && (
                                <div className="mt-4">
                                    <button
                                        className="btn"
                                        onClick={() => document.getElementById(`mentor_modal_${phase.id}`).showModal()}
                                    >
                                        Mentor Review
                                    </button>
                                    <dialog id={`mentor_modal_${phase.id}`} className="modal">
                                        <div className="modal-box">
                                            <form method="dialog">
                                                <button
                                                    className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕
                                                </button>
                                            </form>
                                            <h3 className="font-bold text-lg">Mentor Review</h3>
                                            <p className="py-4">{phase.mentorReview}</p>
                                            <div className="py-4 flex">
                                                {renderStars(phase.mentorRating)}
                                            </div>
                                        </div>
                                    </dialog>
                                </div>
                            )}

                            {phase.menteeRating !== null && (
                                <div className="mt-4">
                                    <button
                                        className="btn"
                                        onClick={() => document.getElementById(`mentee_modal_${phase.id}`).showModal()}
                                    >
                                        Mentee Review
                                    </button>
                                    <dialog id={`mentee_modal_${phase.id}`} className="modal">
                                        <div className="modal-box">
                                            <form method="dialog">
                                                <button
                                                    className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕
                                                </button>
                                            </form>
                                            <h3 className="font-bold text-lg">Mentee Review</h3>
                                            <p className="py-4">{phase.menteeReview}</p>
                                            <div className="py-4 flex">
                                                {renderStars(phase.menteeRating)}
                                            </div>
                                        </div>
                                    </dialog>
                                </div>
                            )}
                        </li>
                    ))}
                </ul>
            </div>

            {isModalOpen && (
                <ReviewModal
                    onClose={() => setIsModalOpen(false)}
                    onSubmit={handleModalSubmit}
                />
            )}
        </div>
    );


};

const ReviewModal = ({onClose, onSubmit}) => {
    const [review, setReview] = useState("");
    const [rating, setRating] = useState(1);

    const handleSubmit = () => {
        onSubmit(review, rating);
    };

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
            <div className="bg-white p-4 rounded shadow-lg w-96">
                <h2 className="text-xl mb-4">Submit Review</h2>
                <textarea
                    value={review}
                    onChange={(e) => setReview(e.target.value)}
                    className="w-full border rounded p-2 mb-4"
                    placeholder="Write your review..."
                />
                <label>
                    Rating:
                    <input
                        type="number"
                        value={rating}
                        onChange={(e) => setRating(e.target.value)}
                        min="1"
                        max="5"
                        className="ml-2 p-2 border rounded"
                    />
                </label>
                <div className="mt-4 flex justify-end">
                    <button
                        onClick={onClose}
                        className="mr-2 bg-gray-300 p-2 rounded"
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleSubmit}
                        className="bg-blue-500 text-white p-2 rounded"
                    >
                        Submit
                    </button>
                </div>
            </div>
        </div>
    );
};

export default MentorshipDetailPage;
