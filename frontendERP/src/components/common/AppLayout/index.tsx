import type { ReactNode } from "react";
import React from "react";
import Sidebar from "../Sidebar";
import { Outlet } from "react-router-dom";

interface AppLayoutProps {
  children?: ReactNode;
}

export default function AppLayout({ children }: AppLayoutProps): JSX.Element {
  return (
    <div className="app-layout">
      <Sidebar />
      <main className="main-content">
        {children || <Outlet />}
      </main>
    </div>
  );
}
