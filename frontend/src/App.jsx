import {Navigate, Route, Routes} from "react-router-dom";
import LoginPage from "./pages/auth/login/LoginPage";

import {Toaster} from "react-hot-toast";
import {useQuery} from "@tanstack/react-query";
import LoadingSpinner from "./components/common/LoadingSpinner";
import DashBoard from "./pages/home/DashBoard.jsx";
import Sidebar from "./components/common/Sidebar.jsx";

import MentorDetailPage from "./components/mentor/MentorDetailPage.jsx";
import MentorApplicationPage from "./components/mentor/MentorApplicationPage.jsx";
import MentorSearchPage from "./components/mentor/MentorSearchPage.jsx";
import MentorshipDetailPage from "./components/mentorship/MentorshipDetailPage.jsx";
import MentorshipPlanPage from "./components/mentorship/MentorshipPlanPage.jsx";
import CreateTopicPage from "./components/common/CreateTopicPage.jsx";

function App() {
    const {data: authUser, isLoading} = useQuery({
        queryKey: ["authUser"],
        queryFn: async () => {
            try {
                const res = await fetch("/api/verify-token");
                const data = await res.json();
                if (!res.ok) {
                    throw new Error("Something went wrong");
                }
                return data;
            } catch (error) {
                console.log(error)
                return null;
            }
        },
        retry: false,
    });

    if (isLoading) {
        return (
            <div className="h-screen flex justify-center items-center">
                <LoadingSpinner size="lg"/>
            </div>
        );
    }

    return (
        <div className="flex max-w-6xl mx-auto">
            {authUser && <Sidebar/>}
            <Routes>
                <Route
                    path="/"
                    element={authUser ? <DashBoard/> : <Navigate to="/login"/>}
                />
                <Route
                    path="/login"
                    element={!authUser ? <LoginPage/> : <Navigate to="/"/>}
                />
                <Route
                    path="/mentors/:id"
                    element={authUser ? <MentorDetailPage/> : <Navigate to="/login"/>}
                />
                <Route
                    path="/application"
                    element={authUser ? <MentorApplicationPage/> : <Navigate to="/login"/>}
                />
                <Route
                    path="/search"
                    element={authUser ? <MentorSearchPage/> : <Navigate to="/login"/>}
                />
                <Route
                    path="/mentorship/:id"
                    element={authUser ? <MentorshipDetailPage/> : <Navigate to="/login"/>}
                />
                <Route
                    path="/plan/:id"
                    element={authUser ? <MentorshipPlanPage/> : <Navigate to="/login"/>}
                />
                <Route
                    path="/topics"
                    element={authUser ? <CreateTopicPage/> : <Navigate to="/login"/>}
                />

            </Routes>
            <Toaster/>
        </div>
    );



}

export default App;
