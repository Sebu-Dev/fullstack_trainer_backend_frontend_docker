/// <reference types="vitest" />
import react from "@vitejs/plugin-react";
import { defineConfig } from "vitest/config";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: "jsdom",
    setupFiles: ["src/__tests__/setup.ts"],
  },
  base: "/fullstack-trainer/",
});

/*interface ViteConfigInterface {
  mode: string;
}
export default ({ mode }: ViteConfigInterface) => {
  process.env = { ...process.env, ...loadEnv(mode, process.cwd()) };
  console.log(process.env.BASEURL);

  return defineConfig({
    plugins: [react()],
    test: {
      globals: true,
      environment: "jsdom",
      setupFiles: ["src/__tests__/setup.ts"],
    },
    base: process.env.BASEURL,
  });
};
*/
