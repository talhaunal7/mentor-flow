import React, {useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useMutation} from '@tanstack/react-query';
import toast from "react-hot-toast";

const MentorshipPlanPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [phases, setPhases] = useState([{ name: "", endDate: "" }]);

    const formatDateToInstant = (dateString) => {
        const date = new Date(dateString);
        return date.toISOString();
    };

    const mutation = useMutation({
        mutationFn: async (newPhases) => {
            const response = await fetch('/api/phases', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    mentorshipId: parseInt(id),
                    phaseDetails: newPhases.map((phase, index) => ({
                        name: phase.name,
                        priorityId: index + 1,
                        endDate: formatDateToInstant(phase.endDate),
                    })),
                }),
            });
            if (!response.ok) {
                const data=await response.json()
                throw new Error(data.error);
            }
        }, onSuccess: () => {
            toast.success("Phases created successfully!");
            navigate(`/mentorship/${id}`);
        },
        onError: (error) => {
            toast.error(`Error: ${error.message}`);
        },
    });

    const handleAddPhase = () => {
        setPhases([...phases, { name: "", endDate: "" }]);
    };

    const handleChangePhase = (index, field, value) => {
        const newPhases = [...phases];
        newPhases[index][field] = value;
        setPhases(newPhases);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        mutation.mutate(phases);
    };

    return (
        <div className="p-4">
            <h2 className="text-2xl font-bold mb-4">Plan Phases for Mentorship</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                {phases.map((phase, index) => (
                    <div key={index} className="flex space-x-4 items-center">
                        <input
                            type="text"
                            placeholder="Phase Name"
                            value={phase.name}
                            onChange={(e) => handleChangePhase(index, "name", e.target.value)}
                            className="p-2 border rounded-md w-full"
                            required
                        />
                        <input
                            type="datetime-local"
                            value={phase.endDate}
                            onChange={(e) => handleChangePhase(index, "endDate", e.target.value)}
                            className="p-2 border rounded-md w-full"
                            required
                        />
                    </div>
                ))}
                <button
                    type="button"
                    onClick={handleAddPhase}
                    className="bg-blue-500 text-white p-2 rounded-md"
                >
                    Add Phase
                </button>
                <button
                    type="submit"
                    className="bg-green-500 text-white p-2 rounded-md"
                    disabled={mutation.isLoading}
                >
                    {mutation.isLoading ? 'Saving...' : 'Save Phases'}
                </button>
            </form>
        </div>
    );
};

export default MentorshipPlanPage;
