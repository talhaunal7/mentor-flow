const fetchSubtopics = async ({ queryKey }) => {
    const [_key, topicId] = queryKey;
    const response = await fetch(`/api/subtopics/${topicId}`);
    if (!response.ok) {
        throw new Error("Network response was not ok");
    }
    return response.json();
};

export default fetchSubtopics;