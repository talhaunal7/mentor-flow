import React, {useState} from "react";
import {useMutation, useQuery} from "@tanstack/react-query";
import {toast} from "react-hot-toast";
import {useNavigate} from "react-router-dom";
import fetchTopics from "../../hooks/fetchTopics.js";
import fetchSubtopics from "../../hooks/fetchSubtopics.js";


const MentorApplicationPage = () => {
    const [selectedTopicId, setSelectedTopicId] = useState("");
    const [selectedSubtopics, setSelectedSubtopics] = useState([]);
    const navigate =useNavigate()

    const { data: topics, isLoading: topicsLoading } = useQuery({
        queryKey: ["topics"],
        queryFn: fetchTopics,
    });

    const { data: subtopics, isLoading: subtopicsLoading } = useQuery({
        queryKey: ["subtopics", selectedTopicId],
        queryFn: fetchSubtopics,
        enabled: !!selectedTopicId,
    });

    const handleTopicChange = (event) => {
        setSelectedTopicId(event.target.value);
        setSelectedSubtopics([]);
    };

    const handleSubtopicChange = (event) => {
        const value = event.target.value;
        setSelectedSubtopics((prev) =>
            prev.includes(value) ? prev.filter((sub) => sub !== value) : [...prev, value]
        );

    };


    const {
        mutate: applyMutation,
        isPending,
        isError,
        error,
    } = useMutation({
        mutationFn: async (applicationData) => {
            const response = await fetch("/api/mentors", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(applicationData),
            });

            if (!response.ok) {
                const data = await response.json()
                throw new Error(data.error);
            }
        },
        onSuccess: () => {
            toast.success("Successfully applied");
            navigate("/")
        },
        onError: (error) => {
            toast.error(error.message || "Application failed");
        }
    });


    const handleSubmit = (event) => {
        event.preventDefault();
        const applicationData = {
            topicId: selectedTopicId,
            subtopicIds: selectedSubtopics.map(Number),
            description: event.target.description.value,
        };
        applyMutation(applicationData)
    };

    return (
        <>
            <div className="flex-[4_4_0] border-l border-r min-h-screen bg-base-100">
                <div className="flex justify-between items-center p-6 border-b border-gray-700 shadow-lg">
                    <h1 className="font-bold text-2xl">Mentor Application</h1>
                </div>
                <div className="p-6">
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div className="p-4 rounded-lg shadow-sm bg-base-100 border border-gray-700">
                            <label htmlFor="topic" className="block text-m font-medium mb-2">
                                Topic
                            </label>
                            <select
                                id="topic"
                                name="topic"
                                value={selectedTopicId}
                                onChange={handleTopicChange}
                                className="block w-full py-2 px-3 border bg-base-100 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                disabled={topicsLoading}
                            >
                                <option value="">Select a topic</option>
                                {topics?.map((topic) => (
                                    <option key={topic.id} value={topic.id}>
                                        {topic.name}
                                    </option>
                                ))}
                            </select>
                        </div>

                        {selectedTopicId && (
                            <div className="p-4 rounded-lg shadow-sm bg-base-100 border border-gray-700">
                                <label htmlFor="subtopics" className="block text-m font-medium mb-2">
                                    Subtopics
                                </label>
                                <div className="grid grid-cols-2 gap-4">
                                    {subtopicsLoading ? (
                                        <p>Loading subtopics...</p>
                                    ) : (
                                        subtopics?.map((subtopic) => (
                                            <div key={subtopic.id} className="flex items-center">
                                                <input
                                                    type="checkbox"
                                                    id={`subtopic-${subtopic.id}`}
                                                    name="subtopics"
                                                    value={subtopic.id}
                                                    checked={selectedSubtopics.includes(String(subtopic.id))}
                                                    onChange={handleSubtopicChange}
                                                    className="h-4 w-4 text-indigo-600 rounded focus:ring-indigo-500 focus:border-indigo-500"
                                                />
                                                <label
                                                    htmlFor={`subtopic-${subtopic.id}`}
                                                    className="ml-2 block text-sm"
                                                >
                                                    {subtopic.name}
                                                </label>
                                            </div>
                                        ))
                                    )}
                                </div>
                            </div>
                        )}

                        <div className="p-4 rounded-lg shadow-sm bg-base-100 border border-gray-700">
                            <label htmlFor="description" className="block text-m font-medium mb-2">
                                Description
                            </label>
                            <textarea
                                id="description"
                                name="description"
                                rows="4"
                                className="block w-full py-2 px-3 border border-gray-700 bg-base-100 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                placeholder="Describe your experience..."
                                required
                            ></textarea>
                        </div>

                        <div className="flex justify-end">
                            <button
                                type="submit"
                                className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                            >
                                Submit Application
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );



};

export default MentorApplicationPage;
