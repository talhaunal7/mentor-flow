

const fetchTopics = async () => {
    const response = await fetch("/api/topics");
    if (!response.ok) {
        throw new Error("Network response was not ok");
    }
    return response.json();
};

export default fetchTopics;