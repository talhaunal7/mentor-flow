import React, {useCallback, useRef, useState} from "react";
import {useQuery} from "@tanstack/react-query";
import Mentors from "./Mentors.jsx";
import fetchTopics from "../../hooks/fetchTopics.js";
import fetchSubtopics from "../../hooks/fetchSubtopics.js";
import debounce from "lodash.debounce";


const MentorSearchPage = () => {
    const [selectedTopicId, setSelectedTopicId] = useState("");
    const [selectedSubtopics, setSelectedSubtopics] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");
    const [debouncedQuery, setDebouncedQuery] = useState("");
    const keyStrokeCount = useRef(0);

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
        setDebouncedQuery("")
        setSelectedTopicId(event.target.value);
        setSelectedSubtopics([]);
    };

    const handleSubtopicChange = (event) => {
        const value = event.target.value;
        setDebouncedQuery("")
        setSelectedSubtopics((prev) =>
            prev.includes(value) ? prev.filter((sub) => sub !== value) : [...prev, value]
        );
    };

    const debouncedSetQuery = useCallback(
        debounce((query) => {
            setDebouncedQuery(query);
            keyStrokeCount.current = 0;
        }, 300),
        []
    );


    const handleInputChange = (e) => {
        const query = e.target.value;
        setSearchQuery(query);
        keyStrokeCount.current += 1;

        if (keyStrokeCount.current === 3) {
            debouncedSetQuery(query);

        }
    };

    return (
        <div className="flex-[4_4_0] mr-auto border-r border-gray-700 min-h-screen bg-base-100">
            <div>
                <div className="p-6">

                    <div className="border-b border-gray-700 p-4 rounded-lg shadow-sm bg-base-100">
                        <label className="block text-m font-medium mb-2">
                            Search
                        </label>
                        <input
                            type="text"
                            value={searchQuery}
                            onChange={handleInputChange}
                            placeholder="Search for mentors..."
                            className="border p-3 rounded-md w-full focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                        />
                    </div>

                    <div className="border-b border-gray-700 p-4 rounded-lg shadow-sm bg-base-100 mt-6">
                    <label htmlFor="topic" className="block text-m font-medium mb-2">
                            Topic
                        </label>
                        <select
                            id="topic"
                            name="topic"
                            value={selectedTopicId}
                            onChange={handleTopicChange}
                            className="block w-full py-2 px-3 border border-gray-700 bg-base-100 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
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
                        <div className="border-b border-gray-700 p-4 rounded-lg shadow-sm bg-base-100 mt-6">
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
                                                className="h-4 w-4 text-indigo-600 border-gray-700 rounded focus:ring-indigo-500 focus:border-indigo-500"
                                            />
                                            <label
                                                htmlFor={`subtopic-${subtopic.id}`}
                                                className="ml-2 block text-m"
                                            >
                                                {subtopic.name}
                                            </label>
                                        </div>
                                    ))
                                )}
                            </div>
                        </div>
                    )}

                    <Mentors
                        selectedTopicId={selectedTopicId}
                        selectedSubtopics={selectedSubtopics}
                        searchQuery={debouncedQuery}
                    />
                </div>
            </div>
        </div>
    );

};

export default MentorSearchPage;