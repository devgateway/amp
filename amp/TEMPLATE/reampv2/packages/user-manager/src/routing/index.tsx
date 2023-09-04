import { createBrowserRouter, createMemoryRouter } from "react-router-dom";
import {RoutingStrategies} from "../utils/Constants";
import { routes } from "./routes";

export const createRouter = ({ routingStrategy, initialPathName }:
                                 { routingStrategy: RoutingStrategies, initialPathName: string }) => {
    if (routingStrategy === RoutingStrategies.BROWSER) {
        return createBrowserRouter(routes);
    }

    const initialEntries = [initialPathName]
    return createMemoryRouter(routes, { initialEntries });
}
