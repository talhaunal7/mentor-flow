import React, {useState} from 'react';
import {useMutation, useQuery, useQueryClient} from '@tanstack/react-query';
import toast from "react-hot-toast";

const fetchTopics = async () => {
    const response = await fetch('api/topics');
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

const fetchSubtopics = async (topicId) => {
    const response = await fetch(`api/subtopics/${topicId}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};


const useTopics = () => {
    return useQuery({
        queryKey: ['topics'],
        queryFn: fetchTopics,
    });
};

const useSubtopics = (topicId) => {
    return useQuery({
        queryKey: ['subtopics', topicId],
        queryFn: () => fetchSubtopics(topicId),
        enabled: !!topicId, // only run if topicId is not null or undefined
    });
};

const CreateTopicPage = () => {
    const queryClient = useQueryClient();
    const {data: topics, isLoading: isTopicsLoading} = useTopics();
    const [selectedTopicId, setSelectedTopicId] = useState(null);
    const [newTopicName, setNewTopicName] = useState('');
    const [newSubtopicName, setNewSubtopicName] = useState('');
    const {data: authUser} = useQuery({queryKey: ["authUser"]});

    const {data: subtopics, isLoading: isSubtopicsLoading} = useSubtopics(selectedTopicId);


    const {mutate: topicMutation} = useMutation({
        mutationFn: async (newTopic) => {
            const response = await fetch('api/topics', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(newTopic),
            });
            if (!response.ok) {
                const data = await response.json()
                throw new Error(data.error || 'Failed to create topic');
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['topics']});
            setNewTopicName('');
        }, onError: (e) => {
            toast.error(e.message)

        }
    });

    const {mutate: subtopicMutation} = useMutation({
        mutationFn: async (newSubtopic) => {
            const response = await fetch('api/subtopics', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(newSubtopic),
            });
            if (!response.ok) {
                const data = await response.json()
                throw new Error(data.error || 'Failed to create subtopic');
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['subtopics']});
            setNewSubtopicName(''); // Clear the form
        }, onError: (e) => {
            toast.error(e.message)

        }
    });

    const handleCreateTopic = (e) => {
        e.preventDefault();
        topicMutation({name: newTopicName});
    };

    const handleCreateSubtopic = (e) => {
        e.preventDefault();
        if (!selectedTopicId) return alert('Please select a topic first');
        const selectedTopicName = topics.find((topic) => topic.id === selectedTopicId).name;
        subtopicMutation({topic: selectedTopicName, name: newSubtopicName});
    };

    if (isTopicsLoading) return <div>Loading topics...</div>;

    if (authUser.role !== "ROLE_ADMINS") {
        return (<div className="container mx-auto p-4">
                Not Authorized
            </div>
        )
    }

    return (
        <div className="container mx-auto p-4">
            <div className="mb-6">
                <h2 className="text-2xl font-semibold mb-4">Create New Topic</h2>
                <form onSubmit={handleCreateTopic} className="flex gap-4">
                    <input
                        type="text"
                        value={newTopicName}
                        onChange={(e) => setNewTopicName(e.target.value)}
                        placeholder="Enter new topic name"
                        className="p-2 border border-gray-300 rounded-md"
                    />
                    <button
                        type="submit"
                        className="p-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
                    >
                        Create Topic
                    </button>
                </form>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {topics.map((topic) => (
                    <div key={topic.id}>
                        <div
                            className="cursor-pointer p-4 bg-blue-100 rounded-lg shadow-md hover:bg-blue-200"
                            onClick={() => setSelectedTopicId(topic.id)}
                        >
                            <h2 className="text-xl font-semibold">{topic.name}</h2>
                        </div>
                        {selectedTopicId === topic.id && (
                            <div className="mt-2 ml-4">
                                <h3 className="text-lg font-semibold">Subtopics</h3>
                                {isSubtopicsLoading ? (
                                    <div>Loading subtopics...</div>
                                ) : (
                                    <ul className="list-disc list-inside">
                                        {subtopics.map((subtopic) => (
                                            <li key={subtopic.id} className="text-gray-700">
                                                {subtopic.name}
                                            </li>
                                        ))}
                                    </ul>
                                )}
                                <div className="mt-4">
                                    <form onSubmit={handleCreateSubtopic} className="flex gap-4">
                                        <input
                                            type="text"
                                            value={newSubtopicName}
                                            onChange={(e) => setNewSubtopicName(e.target.value)}
                                            placeholder="Enter new subtopic name"
                                            className="p-2 border border-gray-300 rounded-md"
                                        />
                                        <button
                                            type="submit"
                                            className="p-2 bg-green-500 text-white rounded-md hover:bg-green-600"
                                        >
                                            Create Subtopic
                                        </button>
                                    </form>
                                </div>
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default CreateTopicPage;
