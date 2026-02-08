import Sidebar from "./Sidebar";
import { Outlet } from "react-router-dom"; // Importe isso!

function AppLayout() {
  return (
    <div className="app-layout">
      <Sidebar />
      <main className="main-content">
        <Outlet /> 
      </main>
    </div>
  );
}

export default AppLayout;