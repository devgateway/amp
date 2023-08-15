import { createBrowserRouter, createMemoryRouter } from "react-router-dom";
import {RoutingStrategies} from "../utils/Constants";
import routes from "./routes";

export const createRouter = ({ routingStrategy }) => {
    if (routingStrategy === RoutingStrategies.BROWSER) {
        return createBrowserRouter(routes);
    }

    const initialEntries = ["/"]
    return createMemoryRouter(routes, { initialEntries });
}
