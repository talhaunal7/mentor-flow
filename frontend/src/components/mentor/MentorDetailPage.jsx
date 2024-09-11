import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {useNavigate, useParams} from "react-router-dom";
import toast from "react-hot-toast";

const MentorDetailPage = () => {
    const {id} = useParams();
    const {data: authUser} = useQuery({queryKey: ["authUser"]});
    const queryClient = useQueryClient();
    const navigate = useNavigate();

    const {
        data: mentorDetail,
        isLoading,
        refetch,
        isRefetching,
    } = useQuery({
        queryKey: ["mentorDetail", id],
        queryFn: async () => {
            try {
                const res = await fetch(`/api/mentors/${id}`);

                const data = await res.json();
                if (!res.ok) {
                    throw new Error(data.error || "Something went wrong");
                }
                return data;
            }catch (e){
                toast.error(e.message)
                navigate("/")
            }

        }
    });

    const mutation = useMutation({
        mutationFn: async (newStatus) => {
            const res = await fetch(`/api/mentors/${id}/status`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({status: newStatus}),
            });
            if (!res.ok) {
                const data=await res.json()
                throw new Error("Failed to update status");
            }
        },
        onSuccess: () => {
            toast.success("mentor has approved")
            queryClient.invalidateQueries({queryKey: ["mentorDetail", id]});
        },
        onError: (error) => {
            toast.error(error.message);
        },
    });

    const pickMentorMutation = useMutation({
        mutationFn: async () => {
            const res = await fetch(`/api/mentorships/pick-mentor/${id}`, {
                method: "POST",
            });
            if (!res.ok) {
                const data = await res.json()
                throw new Error(data.error);
            }
            return res;
        },
        onSuccess: () => {
            toast.success("Mentor picked successfully!");
            navigate(`/`);
        },
        onError: (error) => {
            toast.error(error.message);
        },
    });

    const handleStatusChange = (newStatus) => {
        mutation.mutate(newStatus);
    };

    const handlePickMentor = () => {
        pickMentorMutation.mutate();
    };

    if (isLoading || isRefetching) {
        return (
            <div className="flex justify-center items-center h-screen">
                <div className="text-lg text-center">Loading...</div>
            </div>
        );
    }


    return (
        <div className="container mx-auto p-4">
            <div className="bg-white shadow-md rounded-lg p-6">
                <h1 className="text-3xl font-bold mb-6">Mentor Details</h1>
                <div className="mb-6">
                    <p className="text-xl mb-4">
                        <strong>Name:</strong> {mentorDetail.name} {mentorDetail.surname}
                    </p>
                    <p className="text-xl mb-4">
                        <strong>Topic:</strong> {mentorDetail.topic}
                    </p>
                    <p className="text-xl mb-4">
                        <strong>Subtopics:</strong> {mentorDetail.subtopics.join(", ")}
                    </p>
                    <p className="text-xl mb-4">
                        <strong>Description:</strong> {mentorDetail.description}
                    </p>
                    <p className="text-xl mb-6">
                        <strong>Status:</strong>
                        <span
                            className={`px-3 py-1 rounded-full text-xl font-medium ${
                                mentorDetail?.status === 'HOLD'
                                    ? 'bg-yellow-100 text-yellow-800'
                                    : mentorDetail?.status === 'REJECTED'
                                        ? 'bg-blue-100 text-blue-800'
                                        : mentorDetail?.status === 'APPROVED'
                                            ? 'bg-green-100 text-green-800'
                                            : ''
                            }`}
                        >
                        {mentorDetail?.status === 'HOLD' && 'On Hold'}
                            {mentorDetail?.status === 'REJECTED' && 'Rejected'}
                            {mentorDetail?.status === 'APPROVED' && 'Approved'}
                    </span>
                    </p>
                </div>

                <div className="flex space-x-4">
                    {authUser.role === "ROLE_ADMINS" ? (
                        <>
                            <button
                                className={`btn ${
                                    mentorDetail.status === "APPROVED"
                                        ? "btn-success"
                                        : "btn-outline btn-success"
                                }`}
                                onClick={() => handleStatusChange("APPROVED")}
                                disabled={mentorDetail.status !== "HOLD"}
                            >
                                Approve
                            </button>
                            <button
                                className={`btn ${
                                    mentorDetail.status === "REJECTED"
                                        ? "btn-error"
                                        : "btn-outline btn-error"
                                }`}
                                onClick={() => handleStatusChange("REJECTED")}
                                disabled={mentorDetail.status !== "HOLD"}
                            >
                                Reject
                            </button>
                        </>
                    ) : (
                        <button
                            className={`btn btn-primary ${
                                pickMentorMutation.isLoading ? "btn-disabled" : ""
                            }`}
                            onClick={handlePickMentor}
                            disabled={pickMentorMutation.isSuccess}
                        >
                            {pickMentorMutation.isSuccess ? "Mentor Picked" : "Pick Mentor"}
                        </button>
                    )}
                </div>
            </div>
        </div>
    );


};

export default MentorDetailPage;
