"use client";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { PropsWithChildren } from "react";
import { SidebarProvider } from "./ui/sidebar";
import { Toaster } from "./ui/toaster";

const queryClient = new QueryClient();

export default function Providers({ children }: PropsWithChildren) {
  return (
    <QueryClientProvider client={queryClient}>
      <Toaster />
      <SidebarProvider>{children}</SidebarProvider>
    </QueryClientProvider>
  );
}
